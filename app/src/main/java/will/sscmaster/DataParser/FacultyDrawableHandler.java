package will.sscmaster.DataParser;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

import will.sscmaster.R;

/**
 * Created by Will on 2018/2/23.
 */

public class FacultyDrawableHandler {
    public final static String SCIENCE = "Faculty of Science";
    public final static String ARTS = "Faculty of Arts";
    public final static String SAUDER = "Sauder School of Business";
    public final static String ENGENDERING = "Faculty of Applied Science";
    public final static String EDUCATION = "Faculty of Education";
    public final static String FORESTRY = "Faculty of Forestry";
    public final static String LFS = "Faculty of Land and Food System";
    public final static String DENTISTRY = "Faculty of Dentistry";
    public final static String MUSIC = "School of Music";
    public final static String LAW = "Peter A. Allard School of Law";
    public final static String OTHERS = "Others";

    public final static String SCIENCE_FILE = "science";
    public final static String ARTS_FILE = "arts";
    public final static String SAUDER_FILE = "sauder";
    public final static String ENGENDERING_FILE = "engineering";
    public final static String EDUCATION_FILE = "edu";
    public final static String FORESTRY_FILE = "forestry";
    public final static String LFS_FILE = "lfs";
    public final static String DENTISTRY_FILE = "dentistry";
    public final static String MUSIC_FILE = "music";
    public final static String LAW_FILE = "law";
    public final static String OTHERS_FILE = "others";

    private static Map<String, Drawable> background = new HashMap<>();
    private static Context context;

    public static Drawable getDrawable(Context currContext, String faculty) {
        context = currContext;
        preHandleDrawable();
        Drawable drawable = getDrawable(faculty);
        context = null;
        return drawable;
    }

    private static Drawable getDrawable(String faculty) {
        switch(faculty) {
            case SCIENCE:
                return background.get(SCIENCE_FILE);
            case ARTS:
                return background.get(ARTS_FILE);
            case DENTISTRY:
                return background.get(DENTISTRY_FILE);
            case ENGENDERING:
                return background.get(ENGENDERING_FILE);
            case EDUCATION:
                return background.get(EDUCATION_FILE);
            case SAUDER:
                return background.get(SAUDER_FILE);
            case FORESTRY:
                return background.get(FORESTRY_FILE);
            case LFS:
                return background.get(LFS_FILE);
            case MUSIC:
                return background.get(MUSIC_FILE);
            case LAW:
                return background.get(LAW_FILE);
            default:
                return background.get(OTHERS_FILE);
        }
    }

    private static void preHandleDrawable() {
        background = new HashMap<>();
        background.put(SCIENCE_FILE, context.getDrawable(R.drawable.science));
        background.put(SAUDER_FILE, context.getDrawable(R.drawable.sauder));
        background.put(ENGENDERING_FILE, context.getDrawable(R.drawable.engineering));
        background.put(ARTS_FILE, context.getDrawable(R.drawable.arts));
        background.put(FORESTRY_FILE, context.getDrawable(R.drawable.forestry));
        background.put(DENTISTRY_FILE, context.getDrawable(R.drawable.dentistry));
        background.put(LAW_FILE, context.getDrawable(R.drawable.law));
        background.put(EDUCATION_FILE, context.getDrawable(R.drawable.edu));
        background.put(MUSIC_FILE, context.getDrawable(R.drawable.music));
        background.put(LFS_FILE, context.getDrawable(R.drawable.lfs));
        background.put(OTHERS_FILE, context.getDrawable(R.drawable.others));
    }
}
