;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-contrabassoon
  (:use [sample-utils]
        [overtone.live]))

(def contrabassoon-samples (path-to-described-samples (str *sampleroot* "/contrabassoon")))
(def defaults (array-map :note "F5" :duration "15" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys contrabassoon-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def contrabassoon (play-gen contrabassoon-samples defaults features distance-maxes))
