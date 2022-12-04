package domain;

public class User extends Entity<Long> {
    private String name;
    private String password;
    private String email;

    /**
     * Constructorul implicit
     */
    public User() {}

    /**
     * Constructorul cu parametri
     * @param id - id-ul user-ului
     */
    public User(Long id){
        super(id);
    }

    /**
     * Constructorul cu parametri
     * @param name - numele user-ului
     * @param password - parola user-ului
     * @param email - email-ul user-ului
     */
    public User(String name, String password, String email) {
        super();
        this.name = name;
        this.password = password;
        this.email = email;
    }

    /**
     * Constructorul cu parametri
     * @param id - id-ul user-ului
     * @param name - numele user-ului
     * @param password - parola user-ului
     * @param email - email-ul user-ului
     */
    public User(Long id, String name, String password, String email) {
        super(id);
        this.name = name;
        this.password = password;
        this.email = email;
    }

    /**
     * getter pentru numele user-ului
     * @return numele user-ului
     */
    public String getName() {
        return name;
    }

    /**
     * getter pentru parola user-ului
     * @return parola user-ului
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter pentru email-ul user-ului
     * @return email-ul user-ului
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter pentru numele user-ului
     * @param name - numele user-ului
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter pentru parola user-ului
     * @param password - parola user-ului
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * setter pentru email-ul user-ului
     * @param email - email-ul user-ului
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * transforma un user intr-un string
     * @return string-ul care reprezinta user-ul
     */
    @Override
    public String toString() {
        return "User [id=" + getId() + ", name=" + name + ", password=" + password + ", email=" + email + "]";
    }
}
