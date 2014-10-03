package asati.chektheroad;

import android.app.Application;
import android.graphics.Typeface;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

/**
 * Created by ani110ani on 10/2/14.
 */
public class CheckTheRoadApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        // Initialize typeface helper
        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "bpg_ingiri_arial.ttf"))
                .create();
        TypefaceHelper.init(typeface);
    }
}
