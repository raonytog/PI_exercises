package br.ufes.inf.pi1;
import java.util.Comparator;

public class ComparaPais implements Comparator<Pais> {
    @Override
    public int compare(Pais p1, Pais p2) {
        return -1*p1.getExpecVida().compareTo(p2.getExpecVida());
    }
}