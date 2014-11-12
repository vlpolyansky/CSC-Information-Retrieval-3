package csc.vlpol.infret;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;

public class MeasureMethods {

    public static double dcg(int[] scores) {
        double dcg = scores[0];
        for (int i = 1; i < scores.length; ++i) {
            dcg += scores[i] * Math.log(2) / Math.log(i + 1);
        }
        return dcg;
    }

    public static double ndcg(int[] scores) {
        int[] scoresSorted = Arrays.copyOf(scores, scores.length);
        Arrays.sort(scoresSorted);
        for (int i = 0; i < scoresSorted.length / 2; ++i) {
            int t = scoresSorted[i];
            scoresSorted[i] = scoresSorted[scoresSorted.length - i - 1];
            scoresSorted[scoresSorted.length - i - 1] = t;
        }
        return dcg(scores) / dcg(scoresSorted);
    }

    public static double pfound(int[] scores) {
        double pLook = 1.0;
        double pRel = pRel(scores[0], maxRel);
        double pfound = pLook * pRel;
        for (int i = 1; i < scores.length; ++i) {
            pLook *= (1 - pRel) * (1 - pBreak);
            pRel = pRel(scores[i], maxRel);
            pfound += pLook * pRel;
        }
        return pfound;
    }

    private static double pRel(int rel, int maxRel) {
        return (double)((1 << rel) - 1) / (1 << maxRel);
    }

    public static int[] readScores() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(in.readLine());
            int[] scores = new int[n];
            StringTokenizer tok = new StringTokenizer(in.readLine());
            for (int i = 0; i < n; ++i) {
                scores[i] = Integer.parseInt(tok.nextToken());
            }
            return scores;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        int[] scores = readScores();
        if (scores == null) {
            return;
        }
        double dcg = dcg(scores);
        double ndcg = ndcg(scores);
        double pfound = pfound(scores);
        System.out.printf("Measures:\nDCG = %.5f\nNDCG = %.5f\nPFound = %.5f\n", dcg, ndcg, pfound);
    }

    private static final double pBreak = 0.15;
    private static final int maxRel = 3;
}
