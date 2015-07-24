;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-double-bass
  (:use [sample-utils]
        [overtone.live]))

(def double-bass-samples (path-to-described-samples (str sampleroot "/double-bass")))
(def defaults (array-map :note "F3" :duration "15" :loudness "forte" :style "arco-normal"))
(def features (featureset [:note :duration :loudness :style] (keys double-bass-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})

(def ^:private tmp (play-gen double-bass-samples defaults features distance-maxes))
(def double-bass (:ugen tmp))
(def double-bass-inst (:effect tmp))
(def double-bassi (:effect tmp))
