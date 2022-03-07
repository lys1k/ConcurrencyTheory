import java.io.{FileNotFoundException, IOException}
import scala.collection.mutable
import scala.io.Source

object Traces {
  val letters:String = "abcdefghijklmnopqrstuvwxyz"
  var alphabet:String = ""
  var word: String = ""
  var transaction:String = ""
  var transactions = Array.empty[String]

  def loadData(path: String): Unit = {
    try {
      val source = Source.fromFile(path)
      for (line <- source.getLines()) {
        println(line)
        // wczytanie alfabetu
        if (line.contains("A") && line.contains("=")) {
          for (i <- 0 until line.length) {
            if (letters.contains(line(i))) {
              alphabet += line(i)
            }
          }
        }
        if (line.contains("w") && line.contains("=") && !line.contains(":=")) {
          val tempSplit = line.split("=")
          for (i <- line.length - tempSplit(1).length - 1 until line.length) { // zaczynam sprawdzac po znaku = dlatego line.length - tempSplit(1).length - 1
            if (letters.contains(line(i))) {
              word += line(i)
            }
          }
        }
        if (line.contains('(') && line.contains(')') && line.contains(":=")) {
          transaction = ""
          for (i <- 0 until line.length) {
            if (letters.contains(line(i))) {
              transaction += line(i)
            }
          }
          transactions = transactions :+ transaction
        }
      }
      println()
      source.close()

    } catch {
      case _: FileNotFoundException => println("File not found exception")
      case _: IOException => println("IOException")
        System.exit(-1)
    }
  }

  def getDotFormat(vertices: Array[String], dependencyGraph: List[(String, String)]): String = {
    var dot = "digraph g{\n"
    for(v <- dependencyGraph) {
      dot += v._1(1) + " -> " + v._2(1) + "\n"
    }

    for(v <- vertices) {
      dot += v(1) + "[label=" + v(0) + "]\n"
    }
    dot += "}"
    dot
  }

  def getFNFFromTraces(setI: IndexedSeq[(Char, Char)]): String = {
    var stacks = Map[Char, mutable.Stack[Char]]()   // element mapy stosu to stos dla danego elementu naszego alfabetu
    for(a <- alphabet) stacks += (a -> mutable.Stack[Char]())

    val reversedWord = word.reverse
    for(t1 <- reversedWord) {
      stacks(t1).push(t1)
      for(t2 <- alphabet) {
        if((stacks(t2) ne stacks(t1)) && !setI.contains((t1 , t2))) {
          stacks(t2).push('*')
        }
      }
    }
    // stack._1 element alfabetu
    // stack._2 stos dla tego elementu
    var FNF = ""
    while(stacks.nonEmpty) {
      var currentClass = ""
      for(stack <- stacks) {
        if(stack._2.isEmpty) {
          stacks = stacks.-(stack._1)
        }
        else {
          if(stack._2.top != '*') {
            currentClass += stack._2.pop()
          }
        }
      }
      for(c <- currentClass) {
        for (stack <- stacks) {
          if (stack._2.nonEmpty
            && (stack._2 ne stacks(c))
            && !setI.contains((stack._1, c))
            && stack._2.top == '*') {
            stack._2.pop()
          }
        }
      }
      if(currentClass != "")
        FNF += "[" + currentClass.toSeq.sortWith(_.compareTo(_) < 0).unwrap + "]"
    }
    FNF
  }

  def getFNFFromGraph(v: Array[String], dG: List[(String, String)]): String = {
    var FNF = ""
    var vertices = v
    var dependencyGraph = dG

    while(vertices.nonEmpty) {
      var currentClass = ""
      var currentClassVertices = Array[String]()

      for(vertex <- vertices) {
        var hasPrevious = false
        for(b <- dependencyGraph if !hasPrevious) {
          if(b._2 == vertex) {
            hasPrevious = true
          }
        if(!hasPrevious) {
          currentClass += vertex(0)
          currentClassVertices = currentClassVertices :+ vertex
          vertices = vertices.filter(_ != vertex)
        }
      }
      for(c <- currentClassVertices) {
        dependencyGraph = dependencyGraph.filter(_._1 != c)
      }
      if(currentClass != "") {
        FNF += "[" + currentClass.toSeq.sortWith(_.compareTo(_)<0).unwrap +"]"
      }
    }
    FNF
  }


  def main(args: Array[String]) = {
    if(args.length > 0) {
      println("Data:")
      loadData(args(0))
    }

    else {
      println("Data:")
      loadData("example1.txt")
    }
    if(transactions.length == 0 || alphabet == "" || word == "") {
      println("Not proper data")
      System.exit(-1)
    }

    val D = for(transaction1 <- transactions; transaction2 <- transactions
                if transaction1.slice(2, transaction1.length).contains(transaction2(1))  ||
                  transaction2.slice(2, transaction2.length).contains(transaction1(1)) )
    yield (transaction1(0), transaction2(0))

    val allRelations = for(t1 <- alphabet; t2 <- alphabet) yield (t1, t2)

    val I = for(t <- allRelations if !D.contains(t)) yield t

    println("D = { " + D.mkString(", ") + " }")
    // wypisanie relacji niezaleznosci
    println("I = { " + I.mkString(", ") + " }")

    val foataNormalForm = getFNFFromTraces(I)
    println("FNF computed from trace :\nFNF = " + foataNormalForm + "\n")

    var vertices = Array[String]()
    var dependencyGraph: List[(String, String)] = List[(String, String)]()

    for(i <- word.length to 1 by -1) {
      val current = word(i-1)
      val ver = current.toString + i
      for(vertex <- vertices) {
        if(D.contains((vertex(0), current))) {
          dependencyGraph = (ver, vertex) +: dependencyGraph
        }
      }
      vertices = ver +: vertices
    }
    for(v1 <- vertices; v2 <- vertices; v3 <- vertices) {
      if(dependencyGraph.contains((v1,v2)) && dependencyGraph.contains((v2,v3))) {
        dependencyGraph = dependencyGraph.filter(_ != (v1,v3))
      }
    }

    val dotFormat = getDotFormat(vertices, dependencyGraph)
    println("Graph in dot format:\n" + dotFormat)

    val graphFoataForm = getFNFFromGraph(vertices, dependencyGraph)
    println("FNF computed from graph:\nFNF = " + graphFoataForm)
  }
}