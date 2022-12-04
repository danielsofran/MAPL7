package domain.parser;

import domain.User;

public class UserParser extends Parser<User> {
    /**
     * parseaza un string in entitatea de tip User
     * @param strings - vectorul de stringuri
     * @return entitatea de tip User
     */
    @Override
    public User parse(String[] strings) {
        checkNumber(strings, 4, "User");
        User user = new User();
        user.setId(parseId(strings[0]));
        user.setName(strings[1]);
        user.setPassword(strings[2]);
        user.setEmail(strings[3]);
        return user;
    }
}
