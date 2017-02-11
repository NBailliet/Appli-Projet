package com.example.nicolas.smartride2.Fragments;

/**
 * Created by Valentin on 10/02/2017.
 */
    /*----------Listener class to get coordinates ------------- */
public class MyLocationListener /*implements LocationListener*/ {

       //@Override
/*        public void onLocationChanged(Location loc) {

            Toast.makeText(getContext(),"Location changed : Lat: " + loc.getLatitude()+ " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);

    /*----------to get City-Name from coordinates ------------- */
         /*   String cityName=null;
            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude+"\n"+latitude +
                    "\n\nMy Currrent City is: "+cityName;
            Log.v(TAG, s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }*/
}
