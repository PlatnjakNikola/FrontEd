package production.model;


import java.io.Serializable;
import java.time.LocalDate;

public class Authors extends Entitet implements Serializable {
    String name, surname;
    LocalDate dateOfBirth;

    public Authors(Builder builder) {
        super(builder.id );
        this.name= builder.name;
        this.surname = builder.surname;
        this.dateOfBirth = builder.dateOfBirth;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName(){
        return name + ' ' + surname;
    }
    public String getFullNameWithId(){
        return getId()+ "." + name + ' ' + surname;
    }

    public String getAllDetails(){
        return getFullName() + ", " + getDateOfBirth();
    }

    public static class Builder{
        private final long id;
        private String name;
        private String surname;

        private LocalDate dateOfBirth;

        public Builder(long id){
            this.id = id;
        }
        public Builder withIme(String ime){
            this.name = ime;
            return this;
        }
        public Builder withSurname(String surname){
            this.surname = surname;
            return this;
        }
        public Builder withDateOfBirth(LocalDate dateOfBirth){
            this.dateOfBirth = dateOfBirth;
            return this;
        }
        public Authors build(){return new Authors(this);}

    }


}
