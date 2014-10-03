package asati.chektheroad;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {

    public static enum Type {
        CRASH(R.drawable.ic_road), PIPELINE(R.drawable.icon_water), GARBAGE(R.drawable.ic_polution), OTHER(R.drawable.ic_more);

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
