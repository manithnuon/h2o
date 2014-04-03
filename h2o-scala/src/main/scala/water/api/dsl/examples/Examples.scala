package water.api.dsl.examples

import water.{Boot, H2O, Iced}
import water.api.dsl.{Row, T_T_Collect, DFrame}
import java.util.Random
import hex.drf.DRF

/**
 * Simple example project.
 *
 * Ideas:
 *  - GroupBy for H2O.
 *  - Histogram.
 *  - Matrix product.
 */
object Examples {
  /** Compute average for given column. */
  def example1() = {
    banner(1, "Compute average of 2nd column in cars dataset")
    import water.api.dsl.H2ODsl._
    
    /** Mutable class */
    class Avg(var sum:scala.Double, var cnt:Int) extends Iced;
    
    val f = parse(ffind("smalldata/cars.csv"))
    val r = f collect ( new Avg(0,0), 
      new T_T_Collect[Avg] {
	      override def apply(acc:Avg, rhs:Row):Avg = {
	        acc.sum += rhs.d(2)
	        acc.cnt += 1
	        return acc
	      }
	      override def reduce(l:Avg,r:Avg) = new Avg(l.sum+r.sum, l.cnt+r.cnt)
    	} ) 
    
    println("Average of 2. column is: " + r.sum / r.cnt);
  }
  
  /** Call DRF, make a model, predict on a train data, compute MSE. */ 
  def example2() = {
    banner(2, """The example calls DRF API and produces a forest of regression trees for cars dataset.
The response column represents number of cylinders for each car included in train data. Example then
makes a prediction over train data and compute MSE of prediction."""")

    import water.api.dsl.H2ODsl._
    val f = parse(ffind("smalldata/cars.csv"))
    val params = (p:DRF) => { import p._
      ntrees = 10
      classification = false
      p
    }
    // build a model
    val model = drf(f, null, 3 to 7, 2, params)  // doing regression
    println("The DRF model is: \n" + model)
    // make a prediction
    val predict:DFrame = model.score(f.frame())
    
    println("Prediction on train data: \n" + predict)

    val response = f(2) // take response from input training data
    val serr = (response - predict)^2 // compute mean squared errors
    println("Errors per row: " + serr)
    // make a sum
    val rss = serr collect (0.0, new CDOp() {
      def apply(acc:scala.Double, rhs:Row) = acc + rhs.d(0)
      def reduce(acc1:scala.Double, acc2:scala.Double) = acc1+acc2
    })

    println("RSS: " + rss)
  }

  /** Simple example of deep learning model builder. */
  def example4() = {
    banner(4, "Call deep learning model builder and validate it on test data.")
    // Import favorite classes
    import water.api.dsl.H2ODsl._
    import hex.deeplearning.DeepLearning
    import hex.deeplearning.DeepLearning.ClassSamplingMethod

    // Parse train dataset
    val ftrain = parse(ffind("smalldata/logreg/prostate.csv"))
    // Create parameters for deep learning
    val params = (p:DeepLearning) => {
      import p._
      epochs = 1.0
      hidden = Array(1+new Random(seed).nextInt(4), 1+new Random(seed).nextInt(6))
      classification = true
      seed = seed
      mini_batch = 0
      force_load_balance = false
      shuffle_training_data = true
      score_training_samples = 0
      score_validation_samples = 0
      balance_classes = true
      quiet_mode = false
      score_validation_sampling = ClassSamplingMethod.Stratified
      p
    }

    val dlModel = deeplearning(ftrain, null, 2 to 8, 1, params)
    println("Resulting model: " + dlModel)
  }

  // Call in the context of main classloader
  def main(args: Array[String]):Unit = {
    Boot.main(classOf[Examples], args)
  }

  // Call in the context of H2O classloader
  def userMain(args: Array[String]):Unit = {
    H2O.main(args)
    try {
      example1()
      example2()
      example4()
    } catch {
      case t:Throwable => t.printStackTrace() // Simple debug
    } finally {
      water.api.dsl.H2ODsl.shutdown()
    }
  }

  // Print simple banner
  private def banner(id:Int, desc: String) = {
    println("\n==== Example #"+id+" ====\n "+desc )
    println(  "====================\n")
  }
}

// Companion class
class Examples {
}

