package ourcompany.mylovepet.main.user;



/**
 * Created by REOS on 2017-05-16.
 */

public class User {

    private static User instance = null;

    public static User getIstance(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }

    private User(){
        pets = new Pet[0];
        petManager = new PetManager();
    }

    private String cookie;
    private Pet[] pets;
    private String name;
    private String sunName;

    private PetManager petManager;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSunName() {
        return sunName;
    }

    public void setSunName(String sunName) {
        this.sunName = sunName;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public PetManager getPetManager(){
        return petManager;
    }

}
