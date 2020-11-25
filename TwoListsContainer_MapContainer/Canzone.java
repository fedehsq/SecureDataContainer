public class Canzone {
    private String title;
    //elemento comune a tutte le Canzone
    private static String extension = ".mp3";
    public Canzone(String title) {
        if(title==null)
            throw new NullPointerException();
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public String getExtension(){
        return extension;
    }

    @Override
    public boolean equals(Object other){
        if(other == null || other.getClass() != getClass()) 
            return false;
        Canzone c = (Canzone) other;
        return this.title.equals(c.getTitle());
    }

    @Override
    public String toString(){
        return title +  extension ;
    }
}
