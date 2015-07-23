;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-bass-clarinet
  (:use [sample-utils]
        [overtone.live]))

(def bass-clarinet-samples (path-to-described-samples (str sampleroot "/bass-clarinet")))
(def defaults (array-map :note "F5" :duration "025" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys bass-clarinet-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def bass-clarinet (play-gen bass-clarinet-samples defaults features distance-maxes))
