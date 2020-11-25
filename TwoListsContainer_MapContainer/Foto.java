public class Foto {
    private String title;
    //elemento comune a tutte le Foto
    private static String extension = ".jpeg";
    public Foto(String title) {
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
        Foto f = (Foto) other;
        return this.title.equals(f.getTitle());
    }

    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public String toString(){
        return  title  + extension ;
    }
}
