package dataProvider;

import java.util.ArrayList;
import java.util.List;

import entity.Terms;

public class TermDataProvider {

    public static List<Terms> getTerms() {

        List<Terms> addTerms = new ArrayList<>();

        addTerms.add(new Terms(1,1, "Spring 2020 Term", "11-01-2019", "04-30-2020"));
        addTerms.add(new Terms(2,1, "Fall 2020 Term", "05-01-2020", "10-31-2020"));
        addTerms.add(new Terms(3, 1,"Spring 2021 Term", "11-01-2020", "04-30-2021"));
        addTerms.add(new Terms(4, 1,"Fall 2021 Term", "05-01-2021", "10-31-2021"));

        return addTerms;
    }

}
