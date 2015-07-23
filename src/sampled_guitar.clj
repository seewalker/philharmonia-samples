;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-guitar
  (:use [sample-utils]
        [overtone.live]))

(def guitar-samples (path-to-described-samples (str sampleroot "/guitar")))
(def defaults (array-map :note "F5" :duration "very-long" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys guitar-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def guitar (play-gen guitar-samples defaults features distance-maxes))
