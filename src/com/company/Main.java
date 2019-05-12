package com.company;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.EvaluateDataset;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            /* Load a dataset */
            Dataset data = FileHandler.loadDataset(new File("data/iris.data"), 4, ",");

            KMeansClustering(data);

            FeatureScoring(data);
            FeatureRanking(data);

            EvaluateClassifierOnDataset(data);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void EvaluateClassifierOnDataset(Dataset data) {
        Classifier knn = new KNearestNeighbors(5);
        knn.buildClassifier(data);
        Dataset dataForClassification = data;

        Map<Object, PerformanceMeasure> pm = EvaluateDataset.testDataset(knn, dataForClassification);
        for(Object o:pm.keySet())
            System.out.println(o+": "+pm.get(o).getAccuracy());
    }

    private static void FeatureRanking(Dataset data) {
        /* Create a feature ranking algorithm */
        RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
        /* Apply the algorithm to the data set */
        svmrfe.build(data);
        /* Print out the rank of each attribute */
        for (int i = 0; i < svmrfe.noAttributes(); i++)
            System.out.println(svmrfe.rank(i));
    }

    private static void FeatureScoring(Dataset data) {
        /* Create a feature scoring algorithm */
        GainRatio ga = new GainRatio();
        /* Apply the algorithm to the data set */
        ga.build(data);
        /* Print out the score of each attribute */
        for (int i = 0; i < ga.noAttributes(); i++)
            System.out.println(ga.score(i));
    }

    private static void KMeansClustering(Dataset data) {
        /* Create a new instance of the KMeans algorithm, with no options
         * specified. By default this will generate 4 clusters. */
        Clusterer km = new KMeans(2);
        /* Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster. */
        Dataset[] clusters = km.cluster(data);

        System.out.println("Number of clusters: " + clusters.length);

        for (int i = 0; i < clusters.length; i++) {
            System.out.println("Size of cluster " + i + ": " +  clusters[i].size());
        }

        for (int i = 0; i < clusters.length; i++) {
            System.out.println("Average value of cluster " + i + ": " +  clusters[i].instance(i).value(i));
        }

        ClusterEvaluation sse= new SumOfSquaredErrors();

        double score=sse.score(clusters);

        System.out.println(score);
    }
}
