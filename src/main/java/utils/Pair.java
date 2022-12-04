package utils;

public class Pair<TF, TS> implements Pereche<TF, TS>{
    private TF first;
    private TS second;

    /**
     * Constructorul cu parametri
     * @param first - primul element din pereche
     * @param second - al doilea element din pereche
     */
    public Pair(TF first, TS second){
        this.first = first;
        this.second = second;
    }

    @Override
    public TF getFirst() {
        return first;
    }

    @Override
    public TS getSecond() {
        return second;
    }

    @Override
    public void setFirst(TF first) {
        this.first = first;
    }

    @Override
    public void setSecond(TS second) {
        this.second = second;
    }
}