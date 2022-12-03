public interface PersonBean {
    enum Gender {MALE, FEMALE}

    String getName();

    Gender getGender();

    String getInterest();

    int getHotOrNotRating();

    void setName(String name);

    void setGender(Gender gender);

    void setInterest(String interest);

    void setHotOrNotRating(int rating);
}
