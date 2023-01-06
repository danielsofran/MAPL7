package domain;

import service.ServiceMesaje;

import java.util.Objects;

public class Mesaj extends Entity<Long> {
    private Long userFrom;
    private Long userTo;
    private String content;

    public Mesaj(Long userFrom, Long userTo, String content)
    {
        super();
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.content = content;
    }

    public Mesaj(Long aLong, Long userFrom, Long userTo, String content) {
        super(aLong);
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.content = content;
    }

    public Long getUserFrom() {
        return userFrom;
    }

    public Long getUserTo() {
        return userTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean contains(User user1, User user2){
        return user1.getId().equals(userFrom) && user2.getId().equals(userTo) ||
                user2.getId().equals(userFrom)&& user1.getId().equals(userTo);
    }

    public boolean isFrom(User user)
    {
        return userFrom.equals(user.getId());
    }

    public boolean isTo(User user){
        return userTo.equals(user.getId());
    }

    public Long getOther(Long id){
        return userFrom.equals(id) ? userTo : userFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesaj)) return false;
        if (!super.equals(o)) return false;
        Mesaj mesaj = (Mesaj) o;
        return getId().equals(((Mesaj) o).getId()) ||
                userFrom.equals(mesaj.userFrom) && userTo.equals(mesaj.userTo) && content.equals(mesaj.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userFrom, userTo, content);
    }

    @Override
    public String toString() {
        return "Mesaj{" +
                "userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", content='" + content + '\'' +
                '}';
    }
}
