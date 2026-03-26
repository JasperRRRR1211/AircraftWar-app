package edu.hitsz.game;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.Dao.Record;
import edu.hitsz.Dao.RecordDao;
import edu.hitsz.Dao.RecordDaoImpl;
import edu.hitsz.Music.MusicThread;
import edu.hitsz.Music.SoundManager;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EliteEnemyPro;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.aircraft_factory.AircraftFactory;
import edu.hitsz.aircraft_factory.BossEnemyFactory;
import edu.hitsz.aircraft_factory.EliteEnemyFactory;
import edu.hitsz.aircraft_factory.EliteEnemyProFactory;
import edu.hitsz.aircraft_factory.MobEnemyFactory;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.observer.BombObserver;
import edu.hitsz.observer.BombPublisher;
import edu.hitsz.shoot.DirectShoot;
import edu.hitsz.shoot.RingShoot;
import edu.hitsz.shoot.ScatterShoot;
import edu.hitsz.supply.BaseSupply;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.SuperFireSupply;

/**
 * 游戏主面板
 */

/**
    SurfaceView: 绘制普通的界面（按钮，文本框）
    Runnable: run
    SurfaceHolder.Callback: 监管界面的状态（创建，变换，拆除）
*/
public class Game extends SurfaceView implements Runnable,SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private boolean mIsRunning; // 安全控制绘图线程的开始和结束
    private Thread mThread;
    private Paint mPaint;
    private Handler handler;

    // 由 AbstractGame 注入参数
    protected int enemyMaxNumber;
    protected int heroShootCycle;
    protected int enemyShootCycle;
    protected double eliteProb;
    protected int enemySpawnCycle;
    protected int bossThreshold;
    protected int bossHp;
    protected boolean hasBoss;
    protected boolean difficultyIncrease;

    private int backGroundTop = 0;

    private final ScheduledExecutorService executorService;

    private int timeInterval = 40; // 刷新间隔
    protected int time = 0;       // 当前时刻
    private int cycleTime = 0;
    private int cycleDuration = 600; // 周期

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<BaseSupply> supplies;

    private boolean bossAlive = false;
    private boolean gameOverFlag = false;
    private int score = 0;

    AircraftFactory mobEnemyFactory = new MobEnemyFactory();
    AircraftFactory eliteEnemyFactory = new EliteEnemyFactory();
    AircraftFactory eliteEnemyProFactory = new EliteEnemyProFactory();
    AircraftFactory bossEnemyFactory = new BossEnemyFactory();

    BombPublisher bombPublisher = new BombPublisher();

    private MusicThread bgmThread;
    private MusicThread bossThread;

    public Game(Context context, Handler handler) {
        super(context);
        this.handler = handler; // 存入成员变量

        heroAircraft = HeroAircraft.getInstance(MainActivity.WINDOW_WIDTH / 2,
                MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 100);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        supplies = new LinkedList<>();

        executorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread t = new Thread(r);
            t.setName("game-action-" + t.getId());
            t.setDaemon(true);
            return t;
        });

        initView();

        //启动触摸监听
        this.setOnTouchListener(new HeroController(this,heroAircraft));

    }
    private void initView(){
        mHolder = getHolder();
        mHolder.addCallback((SurfaceHolder.Callback) this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(60);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mIsRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        mIsRunning = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //
    @Override
    public boolean performClick() {
        // 调用父类处理（如果设置了 onClickListener 会被触发）
        return super.performClick();
    }

    /** 参数初始化，由 AbstractGame 调用 */
    public void initGameParams(int enemyMaxNumber, int heroShootCycle, int enemyShootCycle,
                               double eliteProb, int enemySpawnCycle, int bossThreshold, int bossHp,
                               boolean hasBoss, boolean difficultyIncrease) {
        this.enemyMaxNumber = enemyMaxNumber;
        this.heroShootCycle = heroShootCycle;
        this.enemyShootCycle = enemyShootCycle;
        this.eliteProb = eliteProb;
        this.enemySpawnCycle = enemySpawnCycle;
        this.bossThreshold = bossThreshold;
        this.bossHp = bossHp;
        this.hasBoss = hasBoss;
        this.difficultyIncrease = difficultyIncrease;
    }

    /** 游戏启动 */
    public void action() {

        if (gameOverFlag) {
            return;
        }

        time += timeInterval;

        // 难度调整
        if (this instanceof AbstractGame) {
            ((AbstractGame) this).applyDifficulty();
        }

        // 敌机产生
        if (time % enemySpawnCycle == 0) spawnEnemies();

        // 英雄射击
        if (time % heroShootCycle == 0) heroBullets.addAll(heroAircraft.shoot());

        // 敌机射击
        if (time % enemyShootCycle == 0) {
            for (AbstractAircraft enemy : enemyAircrafts) {
                enemyBullets.addAll(enemy.shoot());
                for (BaseBullet bullet : enemy.shoot()) {
                    if (bullet instanceof BombObserver) {
                        bombPublisher.addObserver((BombObserver) bullet);
                    }
                }
            }
        }

        // Boss生成
        checkBossSpawn();

        // 移动
        bulletsMoveAction();
        aircraftsMoveAction();

        // 碰撞检测
        crashCheckAction();

        // 后处理
        postProcessAction();

    }

    /** 敌机生成 */
    private void spawnEnemies() {
        if (enemyAircrafts.size() >= enemyMaxNumber) return;
        double prob = Math.random();
        AbstractAircraft enemy;
        if (prob < 0.5) {
            MobEnemy mob = (MobEnemy) mobEnemyFactory.createAircraft();
            enemyAircrafts.add(mob);
            bombPublisher.addObserver(mob);
        } else if (prob < eliteProb) {
            EliteEnemy elite = (EliteEnemy) eliteEnemyFactory.createAircraft();
            enemyAircrafts.add(elite);
            bombPublisher.addObserver(elite);
        } else {
            EliteEnemyPro elitePro = (EliteEnemyPro)eliteEnemyProFactory.createAircraft();
            enemyAircrafts.add(elitePro);
            bombPublisher.addObserver(elitePro);
        }
    }

    /** Boss生成 */
    private void checkBossSpawn() {
        if (score >= bossThreshold && !bossAlive && hasBoss) {
            BossEnemy boss = (BossEnemy) bossEnemyFactory.createAircraft();
            boss.setHp(bossHp);
            enemyAircrafts.add(boss);
            bombPublisher.addObserver(boss);
            bossAlive = true;

            // 音乐切换
            SoundManager.stopBackground();
            SoundManager.playBackground("src/videos/bgm_boss.wav", true);
        }

        // 检查 Boss 是否死亡
        boolean bossDied = enemyAircrafts.stream().anyMatch(a -> a instanceof BossEnemy && a.notValid());
        if (bossDied && bossAlive) {
            bossAlive = false;
            SoundManager.stopBackground();
            SoundManager.playBackground("src/videos/bgm.wav", true);
        }
    }

    /** 子弹移动 */
    private void bulletsMoveAction() {
        heroBullets.forEach(BaseBullet::forward);
        enemyBullets.forEach(BaseBullet::forward);
    }

    /** 补给移动 */
    private void suppliesMoveAction() {
        supplies.forEach(BaseSupply::forward);
    }

    /** 飞机移动 */
    private void aircraftsMoveAction() {
        enemyAircrafts.forEach(AbstractAircraft::forward);
    }

    /** 碰撞检测 */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (!bullet.notValid() && heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractAircraft enemy : enemyAircrafts) {
                if (enemy.notValid()) continue;
                if (enemy.crash(bullet)) {
                    enemy.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemy.notValid()) {
                        List<BaseSupply> supplyItem = enemy.GenSupply();
                        if (supplyItem != null) supplies.addAll(supplyItem);
                        suppliesMoveAction();
                        score += 10;
                    }
                }
            }
        }

        // 英雄与敌机相撞
        for (AbstractAircraft enemy : enemyAircrafts) {
            if (enemy.notValid()) continue;
            if (heroAircraft.crash(enemy) || enemy.crash(heroAircraft)) {
                enemy.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }

        // 英雄获得补给
        for (BaseSupply supply : supplies) {
            if (supply.notValid()) continue;
            if (heroAircraft.crash(supply)) {
                SoundManager.playEffect("src/videos/get_supply.wav");
                if (supply instanceof BloodSupply) {
                    System.out.println("加血道具生效");
                    heroAircraft.increaseHp(supply.getHpIncrement());
                } else if (supply instanceof FireSupply) {
                    System.out.println("火力道具生效");
                    heroAircraft.setShootStrategy(new ScatterShoot());
                    scheduleResetShoot();
                } else if (supply instanceof SuperFireSupply) {
                    System.out.println("超级火力道具生效");
                    heroAircraft.setShootStrategy(new RingShoot());
                    scheduleResetShoot();
                } else if (supply instanceof BombSupply) {
                    System.out.println("炸弹道具生效");
                    List<BombObserver> destroyedEnemies = bombPublisher.notifyAndCollectDestroyed();

                    // 为被炸掉的敌机加分
                    for (BombObserver obs : destroyedEnemies) {
                        if (obs instanceof AbstractAircraft) {
                            AbstractAircraft enemy = (AbstractAircraft) obs;
                            // 如果是有效敌机
                            if (!enemy.notValid()) continue;
                            score += 10;
                        }
                    }
                }
                supply.vanish();
            }
        }
    }

    private void scheduleResetShoot() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            heroAircraft.setShootStrategy(new DirectShoot());
            scheduler.shutdown();
        }, 5, TimeUnit.SECONDS);
    }

    /** 后处理 */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(aircraft -> {
            if (aircraft.notValid()) {
                bombPublisher.removeObserver((BombObserver) aircraft);
                return true;
            }
            return false;
        });
        supplies.removeIf(AbstractFlyingObject::notValid);

        // 游戏结束
        if (heroAircraft.getHp() <= 0 && !gameOverFlag) {
            gameOverFlag = true;
            //executorService.shutdown();
            //if (bgmThread != null) bgmThread.stopPlay();
            //if (bossThread != null) bossThread.stopPlay();
            inputPlayerName();

            //结束就停止更新画面
            //mIsRunning = false;
        }
    }

    /** 重绘界面 */
    private void drawGame(Canvas canvas) {

        if (ImageManager.BACKGROUND_IMAGE != null) {
            canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop - MainActivity.WINDOW_HEIGHT, mPaint);
            canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop, mPaint);
        }
        backGroundTop = (backGroundTop + 1) % MainActivity.WINDOW_HEIGHT;

        // 绘制各元素
        paintImage(canvas, enemyBullets);
        paintImage(canvas, heroBullets);
        paintImage(canvas, supplies);
        paintImage(canvas, enemyAircrafts);

        // 绘制英雄主机
        if (ImageManager.HERO_IMAGE != null) {
            canvas.drawBitmap(ImageManager.HERO_IMAGE,
                    heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2f,
                    heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2f, mPaint);
        }

        // 绘制分数和生命
        paintScoreAndLife(canvas);
    }

    private void paintImage(Canvas canvas, List<? extends AbstractFlyingObject> list) {
        for (AbstractFlyingObject obj : list) {
            Bitmap img = obj.getImage(); // 这里需要确保 getImage 返回的是 Android 的 Bitmap
            if (img != null) {
                canvas.drawBitmap(img, obj.getLocationX() - img.getWidth() / 2f,
                        obj.getLocationY() - img.getHeight() / 2f, mPaint);
            }
        }
    }

    private void paintScoreAndLife(Canvas canvas) {
        mPaint.setColor(Color.RED); // 设置字体颜色
        mPaint.setFakeBoldText(true); // 设置粗体
        canvas.drawText("SCORE: " + score, 20, 80, mPaint);
        canvas.drawText("LIFE: " + heroAircraft.getHp(), 20, 140, mPaint);
    }

    /** 游戏结束输入玩家名 */
    private void inputPlayerName() {
        // Android 中更新 UI 必须在主线程 (UI 线程) 操作
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("游戏结束！");
            builder.setMessage("请输入你的名字保存成绩：");

            final EditText input = new EditText(getContext());
            builder.setView(input);

            builder.setPositiveButton("保存", (dialog, which) -> {
                String playerName = input.getText().toString();
                if (!playerName.trim().isEmpty()) {
                    RecordDao recordDao = new RecordDaoImpl();
                    recordDao.saveRecord(new Record(playerName, score));
                    Toast.makeText(getContext(), "分数已保存！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "未输入名字，分数未保存。", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setCancelable(false); // 强制玩家操作
            builder.show();
        });
    }

    // ===== Getter =====
    public int getBossThreshold() { return bossThreshold; }
    public int getBossHp() { return bossHp; }
    public boolean hasBoss() { return hasBoss; }

    @Override
    public void run() {
        while (mIsRunning) { // 【流程图】判断绘图标志位

            long startTime = System.currentTimeMillis(); // 记录这一帧开始的时间
            try {
                // 【流程图】锁定画布，获取 Canvas 对象
                mCanvas = mHolder.lockCanvas();
                if (mCanvas != null) {
                    synchronized (mHolder) {
                        // action;
                        action();
                        // 【流程图】调用 Canvas API 绘制 UI
                        drawGame(mCanvas);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 【流程图】释放 Canvas 对象，解锁并提交绘制内容
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
            // 3. 控制帧率 (相当于 timeInterval)
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            if (costTime < timeInterval) {
                try {
                    // 如果算得太快，就让线程睡一会儿，保证每帧间隔差不多是 timeInterval(40ms)
                    Thread.sleep(timeInterval - costTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 【流程图】false -> 退出循环 (线程结束)
    }
}