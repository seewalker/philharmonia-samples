;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-mandolin
  (:use [sample-utils]
        [overtone.live]))

(def mandolin-samples (path-to-described-samples (str sampleroot "/mandolin")))
(def defaults (array-map :note "F5" :duration "very-long" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys mandolin-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def mandolin (play-gen mandolin-samples defaults features distance-maxes))
