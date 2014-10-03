package asati.chektheroad;

import android.app.Application;

/**
 * Created by ani110ani on 10/2/14.
 */
public class FontApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        // Initialize typeface helper
        TypefaceCollection typeface = new TypefaceCollection.Builder()
                .set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), ""))
                .create();
        TypefaceHelper.init(typeface);
    }
}
