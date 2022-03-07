import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Graph {
     static public String[] mColors = {
            "#39add1", "#3079ab", "#c25975", "#e15258", "#f9845b", "#838cc7", "#838cc7", "#7d669e", "#53bbb4", "#51b46d",
            "#e0ab18", "#637a91", "#f092b0", "#b7c0c7"
    };

    public static String getRandomColor() {
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);
        return mColors[randomNumber];
    }

    public static void main(String[] args) {
        int n;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        else{
            n = 4;
        }

        StringBuilder graph = new StringBuilder("digraph {" +  "\n");
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                for (int k = i; k <= n + 1; k++) {
                    graph.append("A_").append(i).append("_").append(j).append(" -> B_").append(i).append("_")
                            .append(k).append("_").append(j).append(";\n");
                    graph.append("B_").append(i).append("_").append(k).append("_").append(j).append(" -> C_")
                            .append(i).append("_").append(k).append("_").append(j).append(";\n");
                }
            }
        }
        for (int i1 = 1; i1 < n; i1++) {
            int i2 = i1 + 1;
            int k1 = i2;
            for (int j = i2 + 1; j <= n + 1; j++) {
                for (int k2 = i2 + 1; k2 <= n; k2++) {
                    graph.append("C_").append(i1).append("_").append(j).append("_").append(k1).append(" -> B_")
                            .append(i2).append("_").append(j).append("_").append(k2).append(";\n");
                }
            }
        }
        for (int i1 = 1; i1 < n - 1; i1++) {
            int i2 = i1 + 1;
            int j = i2;
            int k1 = i2;
            for (int k2 = i2 + 1; k2 <= n; k2++) {
                graph.append("C_").append(i1).append("_").append(j).append("_").append(k1).append(" -> A_")
                        .append(i2).append("_").append(k2).append(";\n");
            }
            for (k1 = i2 + 1; k1 <= n; k1++) {
                int k_2 = k1;
                graph.append("C_").append(i1).append("_").append(j).append("_").append(k1).append(" -> A_")
                        .append(i2).append("_").append(k_2).append(";\n");
            }
        }
        for (int i1 = 1; i1 < n - 1; i1++) {
            int i2 = i1 + 1;
            for (int k = i2 + 1; k <= n; k++)
                for (int j = i2 + 1; j <= n + 1; j++)
                    graph.append("C_").append(i1).append("_").append(j).append("_").append(k).append(" -> C_")
                            .append(i2).append("_").append(j).append("_").append(k).append(";\n");
        }

        for (int i = 1; i < n; i++) {
            String color1 = getRandomColor();
            String color2 = getRandomColor();
            String color3 = getRandomColor();

            for (int k = i + 1; k <= n; k++) {
                graph.append("A_").append(i).append("_").append(k).append(" [label=<A<sub>").append(i)
                        .append(",").append(k).append("</sub>>, ").append("fillcolor=\"").append(color1)
                        .append("\", style=filled];\n");
                for (int j = i; j <= n + 1; j++)
                    graph.append("C_").append(i).append("_").append(j).append("_").append(k).append(" [label=<C<sub>")
                            .append(i).append(",").append(j).append(",").append(k).append("</sub>>, ").append("fillcolor=\"")
                            .append(color3).append("\", style=filled];\n").append("B_").append(i).append("_").append(j)
                            .append("_").append(k).append(" [label=<B<sub>").append(i).append(",").append(j).append(",")
                            .append(k).append("</sub>>, ").append("fillcolor=\"").append(color2).append("\", style=filled];\n");
            }
        }
        graph.append("}");
        System.out.println(graph);

        try {
            FileWriter fileWriter = new FileWriter("graph.dot", false);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(graph.toString());
            out.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}