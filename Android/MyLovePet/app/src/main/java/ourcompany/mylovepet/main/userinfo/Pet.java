package ourcompany.mylovepet.main.userinfo;

/**
 * Created by REOS on 2017-05-16.
 */

public class Pet {
    private final int petNo;
    private int petKind;
    private int serialNo;
    private String name;
    private String gender;
    private String birth;
    private String photo_URL;

    private Pet(Builder builder){
        this.petNo = builder.petNo;
        this.petKind = builder.petKind;
        this.serialNo = builder.serialNo;
        this.name = builder.name;
        this.gender = builder.gender;
        this.birth = builder.birth;
        this.photo_URL = builder.photo_URL;
    }

    public int getPetNo() {
        return petNo;
    }

    public int getPetKind() {
        return petKind;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public String getPhoto_URL() {
        return photo_URL;
    }

    public static class Builder{
        private final int petNo;
        private int petKind;
        private int serialNo;
        private String name;
        private String gender;
        private String birth;
        private String photo_URL;

        public Builder(int petNo){
            this.petNo = petNo;
        }

        public Builder petKind(int petKind){
            this.petKind = petKind;
            return this;
        }

        public Builder serialNo(int serialNo){
            this.serialNo = serialNo;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder gender(String gender){
            this.gender = gender;
            return this;
        }

        public Builder birth(String birth){
            this.birth = birth;
            return this;
        }

        public Builder photo_URL(String photo_URL){
            this.photo_URL = photo_URL;
            return this;
        }

        public Pet build(){
            return new Pet(this);
        }

    }


}
