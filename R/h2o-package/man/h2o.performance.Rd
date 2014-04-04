\name{h2o.performance}
\alias{h2o.performance}
\title{
Performance Measures
}
\description{
Evaluate the predictive performance of a model via various measures.
}
\usage{
h2o.performance(data, reference, measure = "accuracy", thresholds)
}
\arguments{
  \item{data}{
  An \code{\linkS4class{H2OParsedData}} object containing the predicted outcomes.
  }
  \item{reference}{
  An \code{\linkS4class{H2OParsedData}} object containing the actual outcomes for comparison.
  }
  \item{measure}{
  (Optional) A character string indicating the performance measure to optimize. Must be one of the following: 
  TODO: Describe each one.
  }
  \item{thresholds}{
  (Optional) A numeric vector indicating the threshold values at which to compute the performance measure. If missing, range will default to TODO: What is the default in H2O?
  TODO: Still not sure I understand what exactly these thresholds are, is it the FPR or something else?
  }
}
\details{
%%  ~~ If necessary, more details than the __description__ above ~~
}
\source{
%%  ~~ reference to a publication or URL from which the data were obtained ~~
}
\references{
%%  ~~ possibly secondary sources and usages ~~
}
\examples{
data(h2o.performance)
## maybe str(h2o.performance) ; plot(h2o.performance) ...
}
\keyword{datasets}
