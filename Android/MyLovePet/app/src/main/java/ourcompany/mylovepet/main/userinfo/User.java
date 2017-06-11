package ourcompany.mylovepet.main.userinfo;

import java.util.ArrayList;

/**
 * Created by REOS on 2017-05-16.
 */

public class User {

    private static User instance = null;

    public static User getIstance(){
        if(instance == null){
            instance = new User();
            return instance;
        }else {
            return instance;
        }
    }

    private String cookie;
    private Pet[] pets;


    private User(){}

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Pet getPet(int position){
        return pets[position];
    }

    public Pet[] getPets() {
        return pets;
    }
    public void setPets(Pet[] pets) {
        this.pets = pets;
    }

}
