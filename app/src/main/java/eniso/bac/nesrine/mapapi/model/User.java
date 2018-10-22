package eniso.bac.nesrine.mapapi.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nesri on 29/10/2017.
 */

public class User {
    String email;
    String name;
    LatLng coordinates;
    public User()
    {
    }

    public User(String name,String email, LatLng coordinates) {
        this.email = email;
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
