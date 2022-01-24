package domain;

public enum RadGroesen {
    SMALL, MEDIUM, LARGE;

    public char getFirstLetter(){
        return this.name().charAt(0);
    }

    public String getFirstLetterAsString(){
        return this.name().substring(0, 1);
    }
}
