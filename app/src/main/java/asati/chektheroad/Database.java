package asati.chektheroad;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {

    public static enum Type {
        CRASH(R.drawable.road), PIPELINE(R.drawable.water), GARBAGE(R.drawable.garbige), OTHER(R.drawable.other);

        public final int resId;

        Type(int resId) {
            this.resId = resId;
        }
    }

    private static final Map<LatLng, Type> mCasesMap = new LinkedHashMap<LatLng, Type>();

    public static void putToMap(LatLng point, Type type) {
        mCasesMap.put(point, type);
    }

    public static java.util.Set<Map.Entry<LatLng, Type>> listEverything() {
        return mCasesMap.entrySet();
    }


}
