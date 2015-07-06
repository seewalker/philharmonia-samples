;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-french-horn
  (:use [sample-utils]
        [overtone.live]))

(def french-horn-samples (path-to-described-samples (str *sampleroot* "/french-horn")))
(def defaults (array-map :note "F5" :duration "025" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys french-horn-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def french-horn (play-gen french-horn-samples defaults features distance-maxes))
