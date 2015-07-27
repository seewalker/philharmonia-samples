;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  philharmonia-samples.sampled-cello
  (:use [philharmonia-samples.sample-utils]
        [overtone.live]))

(def cello-samples (path-to-described-samples (str sampleroot "/cello")))
(def defaults (array-map :note "60" :duration "15" :loudness "forte" :style "arco-normal"))
(def features (featureset [:note :duration :loudness :style] (keys cello-samples)))
(def distance-maxes {:note 1 :duration 4 :loudness 3 :style 6})

(def ^:private tmp (play-gen cello-samples defaults features distance-maxes))
(def cello (:ugen tmp))
(def cello-inst (:effect tmp))
(def celloi (:effect tmp))
