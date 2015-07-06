;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-banjo
  (:use [sample-utils]
        [overtone.live]))

(def banjo-samples (path-to-described-samples (str *sampleroot* "/banjo")))
(def defaults (array-map :note "F5" :duration "very-long" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys banjo-samples)))
(def distance-maxes {:note 1 :duration 4 :loudness 0 :style 6})
(def banjo (play-gen banjo-samples defaults features distance-maxes))
;how to solve the order problem?
(demo (banjo))
(demo (banjo ["G3" "very-long" "forte" "normal"]))
(demo (banjo {:loudness "forte"}))
