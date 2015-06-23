# What does this project do for you?

This project provides one module per sample instrument. Each module provides a function, of the same name as the instrument, which returns a ugen suitable for use with overtone. The 'definst' infrastructure is not used because these sampled instruments have many variants, e.g. vibrato or non-vibrato, of the same note.

Because certain combinations of the features may be absent for 

# How to use the sampled instrument functions

Let's use the example of a cello, because all the samples instruments are similar to this.

##loading samples
(use 'sampled-cello)  ;assuming philharmonia-samples/src is in your classpath.
## calling samples
(cello)               ;produces a ugen playing the default sample.
(demo (cello))        ;will make your speakers play the default sample.
(cello {:note "As3"}) ;you can specify only the parameters you wish.
(cello :note "As3")   ;or, since overtone is meant for live coding, you can save some precious keystrokes by avoiding the explicit passing of a map.
(cello {:note 60})    ;passing an integer will make the value interpreted as a midi note, rather than a note name.
(cello 60)            ;'note' is arguably the most significant feature, so a single scalar argument will be interpreted as a note.
(cello {:note "As3" :loudness "piano" :style "vibrato"])
(cello {:note "As3" :loudness "p" "vibrato"])  ;
(cello ["As3" "piano" "vibrato"]) ;when passing a vector, you must specify all features.
## error conditions
(cello :note 10000000)  ;when requesting a sample that does not exist, a silent ugen is returned and
(cello {:note "G4" :duration "very-very-long"} :autocorrect? true)  ;attempt to 
                                                                    ;
(cello {:note "G4" :duration "very-very-long"} :autocorrect? false) ;
## customizations
(def *cello-autocorrect* false) ;
(def cello-defaults [ ]) ;put something other than
(defn error-action [] (do (println "no such sample") ;override this to get some other behavoir when a requested sample is not available.
                          <silent-ugen>))

# How to get things going:

Overtone requires ".wav" files, but the philharmonia website provides mp3s. I've gone through the work of converting them, and you can download the samples from https://drive.google.com/folderview?id=0B7GoGDjZUyZ5flIxOXNPN2VjdXl2ZXpzTGRQTE9OU3pJYmV4Vlg3VFZRd3hhek5lelNiUVU&usp=sharing.

# Welcome Changes

The 1.0.0 version of this project might involve more precise names for the instrument features. For example, I chose "loudness" as the name for the class of features like "piano" and "forte", and "style" as the name for features like "arco-normal", but this might not be the terminology musicians want to work with.

Of course, more samples and a better multi-gigabye public storage solution would be a plus.
