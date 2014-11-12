Deep Learning for Java
=======================

Deep learning is a form of state-of-the-art machine learning that can learn to recognize patterns in data unsupervised.

Unsupervised pattern recognition saves time during data analysis, trend discovery and labeling of certain types of data, such as image, text, sound and time series.

See [Deeplearning4j.org](http://deeplearning4j.org/) for applications, tutorials, definitions and other resources on the discipline.

Download a release
===========================

Normal development distribution:
https://oss.sonatype.org/content/repositories/snapshots/org/deeplearning4j/deeplearning4j-distribution/0.0.3.2-SNAPSHOT/

Examples distribution:
https://oss.sonatype.org/content/repositories/snapshots/org/deeplearning4j/deeplearning4j-examples/0.0.3.2-SNAPSHOT/

Feature set summary
====================

1. Distributed deep learning via Akka clustering and distributed coordination of jobs via Hazelcast with configurations stored in Apache Zookeeper.

2. Various data-preprocessing tools, such as an image loader that allows for binarization, scaling of pixels, normalization via zero-unit mean and standard deviation.

3. Deep-belief networks for both continuous and binary data. Support for sequential via moving window/viterbi

4. Native matrices via Jblas, a Fortran library for matrix computations. As 1.2.4 - GPUs when nvblas is present.

5. Automatic cluster provisioning for Amazon Web Services' Elastic Compute Cloud (EC2).

6. Baseline ability to read from a variety of input providers including S3 and local file systems.

7. Text processing via Word2Vec as well as a term frequency–inverse document frequency (TFIDF) vectorizer.
          
  - Special tokenizers/stemmers and a SentenceIterator interface to make handling text input agnostic.
  - Ability to do moving-window operations via a Window encoding. Optimized with Viterbi.


Neural net knobs supported
=====================================
         L2 Regualarization
         Dropout
         Adagrad
         Momentum
         Optimization algorithms for training (Conjugate Gradient, Stochastic Gradient Descent)
         Different kinds of activation functions (Tanh, Sigmoid, HardTanh, Softmax)
         Normalization by input rows, or not
         Sparsity (force activations of sparse/rare inputs)
         Weight transforms (useful for deep autoencoders)
         Different kinds of loss functions - squared loss, reconstruction cross entropy, negative log likelihood
         Probability distribution manipulation for initial weight generation


Coming up
=============================

See our wiki: https://github.com/agibsonccc/java-deeplearning/wiki/Deeplearning4j-Roadmap

Contact
=============================

Deeplearning4j has its own IRC channel at https://webchat.freenode.net/, a network intended primarily for developers of free and open source software. Just enter /join #deeplearning4j on IRC where you would normally type to chat. Alternatively, we're also reachable through [Skymind](http://www.skymind.io/contact.html)'s contact page.


GPU Support (work in progress, only scaffolding for now)
==========================================

Initial deployment instructions:

1. git clone git@github.com:MysterionRise/mavenized-jcuda.git
2. cd mavenized-jcuda && mvn clean install -DskipTests
3. Include the linear-algebra-jcublas in your pom:

                   <dependency>
                    <groupId>org.nd4j</groupId>
                    <artifactId>nd4j-jcublas</artifactId>
                    <version>0.0.3.2-SNAPSHOT</version>
                   </dependency>

Bugs/Feature Requests
=============================

https://github.com/agibsonccc/java-deeplearning/issues


Installation
===========================================

# Maven coordinates

 It is highly reccommended that you use development snapshots right now.
 
 Put this snippet in your POM and use the dependencies as versioned below.
 
      <repositories>
            <repository>
             <id>snapshots-repo</id>
              <url>https://oss.sonatype.org/content/repositories/snapshots</url>
              <releases><enabled>false</enabled></releases>
              <snapshots><enabled>true</enabled></snapshots>
            </repository>
       </repositories>

## Singular neural nets
       
       <dependency>
        <groupId>org.deeplearning4j</groupId>
        <artifactId>deeplearning4j-core</artifactId>
        <version>0.0.3.2.5</version>
       </dependency>

## Scaleout for multithreaded methods and clustering
       
        <dependency>
         <groupId>org.deeplearning4j</groupId>
           <artifactId>deeplearning4j-scaleout-akka</artifactId>
         <version>0.0.3.2.5</version>
        </dependency>

## Text analysis

          <dependency>
           <groupId>org.deeplearning4j</groupId>
           <artifactId>deeplearning4j-nlp</artifactId>
           <version>0.0.3.2.5</version>
          </dependency>
