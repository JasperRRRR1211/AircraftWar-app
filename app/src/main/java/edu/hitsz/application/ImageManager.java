package edu.hitsz.application;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log; // 用于Android日志输出，替代printStackTrace

// 替换为你实际的Android应用包名
import edu.hitsz.R;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EliteEnemyPro;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FireSupply;
import edu.hitsz.supply.SuperFireSupply;

import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    private static final String TAG = "ImageManager"; // 用于Logcat的标签
    private static Resources resources; // 用于访问应用程序资源

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_PRO_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;
    public static Bitmap BLOOD_SUPPLY_IMAGE;
    public static Bitmap BOMB_SUPPLY_IMAGE;
    public static Bitmap FIRE_SUPPLY_IMAGE;
    public static Bitmap SUPER_FIRE_SUPPLY_IMAGE;

    /**
     * 初始化 ImageManager。必须在任何图片加载操作之前调用。
     * 建议在 Application 类的 onCreate 方法中调用，并使用 getApplicationContext()。
     *
     * @param context 应用程序上下文
     */
    public static void init(Context context) {
        if (resources == null) {
            resources = context.getResources();
            loadAllImages(); // 首次初始化时加载所有图片
        } else {
            Log.d(TAG, "ImageManager already initialized.");
        }
    }

    /**
     * 根据难度设置背景图片。
     *
     * @param main
     * @param difficulty 难度字符串 ("easy", "normal", "hard")
     */
    public static void setBackground( String difficulty) {
        if (resources == null) {
            Log.e(TAG, "ImageManager has not been initialized. Call init() first.");
            throw new IllegalStateException("ImageManager must be initialized with a Context before loading images.");
        }

        int drawableResId;
        switch (difficulty.toLowerCase()) {
            case "easy":
                drawableResId = R.drawable.bg; // 对应 bg.jpg
                break;
            case "normal":
                drawableResId = R.drawable.bg2; // 对应 bg2.jpg
                break;
            case "hard":
                drawableResId = R.drawable.bg3; // 对应 bg3.jpg
                break;
            default:
                Log.w(TAG, "Unknown difficulty: " + difficulty + ". Loading default background (bg.jpg).");
                drawableResId = R.drawable.bg; // 默认背景 bg.jpg
        }

        BACKGROUND_IMAGE = BitmapFactory.decodeResource(resources, drawableResId);
        if (BACKGROUND_IMAGE == null) {
            Log.e(TAG, "Failed to load background image for difficulty: " + difficulty + " (Resource ID: " + drawableResId + "). Please ensure " + difficulty + " is mapped to an existing drawable file.");
        }
    }

    /**
     * 内部方法，用于加载所有静态图片。
     * 在 init() 方法中调用。
     */
    private static void loadAllImages() {
        if (resources == null) {
            Log.e(TAG, "Resources not initialized. Cannot load images.");
            return;
        }

        // 加载默认背景图。setBackground 可以在游戏开始时根据选择的难度再次调用
        BACKGROUND_IMAGE = loadImage(R.drawable.bg, "BACKGROUND_IMAGE (default)"); // 对应 bg.jpg

        HERO_IMAGE = loadImage(R.drawable.hero, "HERO_IMAGE"); // 对应 hero.png
        MOB_ENEMY_IMAGE = loadImage(R.drawable.mob, "MOB_ENEMY_IMAGE"); // 对应 mob.png
        ELITE_ENEMY_IMAGE = loadImage(R.drawable.elite, "ELITE_ENEMY_IMAGE"); // 对应 elite.png
        ELITE_ENEMY_PRO_IMAGE = loadImage(R.drawable.elite_pro, "ELITE_ENEMY_PRO_IMAGE"); // 对应 elite_pro.png (已是下划线命名)
        BOSS_ENEMY_IMAGE = loadImage(R.drawable.boss, "BOSS_ENEMY_IMAGE"); // 对应 boss.png

        HERO_BULLET_IMAGE = loadImage(R.drawable.bullet_hero, "HERO_BULLET_IMAGE"); // 对应 bullet_hero.png
        ENEMY_BULLET_IMAGE = loadImage(R.drawable.bullet_enemy, "ENEMY_BULLET_IMAGE"); // 对应 bullet_enemy.png

        BLOOD_SUPPLY_IMAGE = loadImage(R.drawable.prop_blood, "BLOOD_SUPPLY_IMAGE"); // 对应 prop_blood.png
        BOMB_SUPPLY_IMAGE = loadImage(R.drawable.prop_bomb, "BOMB_SUPPLY_IMAGE"); // 对应 prop_bomb.png
        FIRE_SUPPLY_IMAGE = loadImage(R.drawable.prop_bullet, "FIRE_SUPPLY_IMAGE"); // 对应 prop_bullet.png
        SUPER_FIRE_SUPPLY_IMAGE = loadImage(R.drawable.prop_bullet_plus, "SUPER_FIRE_SUPPLY_IMAGE"); // 对应 prop_bullet_plus.png (已是下划线命名)

        // 将加载的Bitmap放入CLASSNAME_IMAGE_MAP
        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemyPro.class.getName(), ELITE_ENEMY_PRO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BloodSupply.class.getName(), BLOOD_SUPPLY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(FireSupply.class.getName(), FIRE_SUPPLY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(), BOMB_SUPPLY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(SuperFireSupply.class.getName(), SUPER_FIRE_SUPPLY_IMAGE);

        Log.i(TAG, "All images loaded successfully.");
    }

    /**
     * 辅助方法：加载图片并进行非空检查。
     *
     * @param resId 资源的ID (例如 R.drawable.hero)
     * @param name  图片的逻辑名称 (用于日志输出)
     * @return 加载的Bitmap对象，如果失败则返回null
     */
    private static Bitmap loadImage(int resId, String name) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        if (bitmap == null) {
            Log.e(TAG, "Failed to load image: " + name + " (Resource ID: " + resId + "). Please check if the file exists in res/drawable.");
            // 可以在此处返回一个默认的占位符图片，而不是null
            // 例如：return BitmapFactory.decodeResource(resources, R.drawable.placeholder_error);
        }
        return bitmap;
    }

    /**
     * 根据类名获取对应的图片。
     *
     * @param className 类名的字符串表示
     * @return 对应的Bitmap对象，如果不存在则返回null
     */
    public static Bitmap get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    /**
     * 根据对象获取其所属基类对应的图片。
     *
     * @param obj 任意对象
     * @return 对应的Bitmap对象，如果对象为null或没有对应图片则返回null
     */
    public static Bitmap get(Object obj) {
        if (obj == null) {
            return null;
        }
        return get(obj.getClass().getName());
    }
}
