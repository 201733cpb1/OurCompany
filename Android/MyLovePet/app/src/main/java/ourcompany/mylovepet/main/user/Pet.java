package ourcompany.mylovepet.main.user;

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
    private String photoFileNo;
    private String lastMealDate;
    private int walkCount;

    private Pet(Builder builder){
        this.petNo = builder.petNo;
        this.petKind = builder.petKind;
        this.serialNo = builder.serialNo;
        this.name = builder.name;
        this.gender = builder.gender;
        this.birth = builder.birth;
        this.photoFileNo = builder.photoFileNo;
        this.lastMealDate = builder.lastMealDate;
        this.walkCount = builder.walkCount;
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

    public String getPhotoFileNo() {
        return photoFileNo;
    }

    public String getLastMealDate(){
        return lastMealDate;
    }

    public int getWalkCount(){
        return walkCount;
    }

    public static class Builder{
        private final int petNo;
        private int petKind;
        private int serialNo;
        private String name;
        private String gender;
        private String birth;
        private String photoFileNo;
        private String lastMealDate;
        private int walkCount;

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

        public Builder photoFileNo(String photoFileNo){
            this.photoFileNo = photoFileNo;
            return this;
        }

        public Builder lastMealDate(String lastMealDate){
            this.lastMealDate = lastMealDate;
            return this;
        }

        public Builder walkCount(int walkCount){
            this.walkCount = walkCount;
            return this;
        }

        public Pet build(){
            return new Pet(this);
        }

    }


}
