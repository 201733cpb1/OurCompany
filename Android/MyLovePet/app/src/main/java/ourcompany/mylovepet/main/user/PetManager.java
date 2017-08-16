package ourcompany.mylovepet.main.user;

/**
 * Created by REOS on 2017-08-11.
 */

public class PetManager {

    private Pet[] pets;

    public PetManager(){
        pets = new Pet[0];
    }

    public Pet getPet(int index){
        return pets[index];
    }

    public void setPets(Pet[] pets){
        this.pets = pets;
    }

    public Pet[] getPets(){
        return pets;
    }

    public int getSize(){
        return pets.length;
    }

    public void clear(){
        pets = new Pet[0];
    }
}
