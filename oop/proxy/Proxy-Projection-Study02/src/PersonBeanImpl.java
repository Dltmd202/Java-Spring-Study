public class PersonBeanImpl implements PersonBean{
    private String name;
    private Gender gender;
    private String interest;
    private int rating = 0;
    private int raringCount = 0;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public String getInterest() {
        return interest;
    }

    @Override
    public int getHotOrNotRating() {
        if(rating == 0) return 0;
        return rating/raringCount;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public void setInterest(String interest) {
        this.interest = interest;
    }

    @Override
    public void setHotOrNotRating(int rating) {
        this.rating += rating;
        ++raringCount;
    }
}
