package ourcompany.mylovepet.main.userinfo;

/**
 * Created by REOS on 2017-05-16.
 */

public class Pet {
    private int animalNo;
    private int animalIndex;
    private int serialNo;
    private String name;
    private String gender;
    private String birth;
    private String photo_URL;

    public int getAnimalNo() {
        return animalNo;
    }

    public void setAnimalNo(int animalNo) {
        this.animalNo = animalNo;
    }

    public int getAnimalIndex() {
        return animalIndex;
    }

    public void setAnimalIndex(int animalIndex) {
        this.animalIndex = animalIndex;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoto_URL() {
        return photo_URL;
    }

    public void setPhoto_URL(String photo_URL) {
        this.photo_URL = photo_URL;
    }
}