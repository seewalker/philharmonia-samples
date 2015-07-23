;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  sampled-cor-anglais
  (:use [sample-utils]
        [overtone.live]))

(def cor-anglais-samples (path-to-described-samples (str sampleroot "/cor-anglais")))
(def defaults (array-map :note "F5" :duration "025" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys cor-anglais-samples)))
(def distance-maxes {:note 2 :duration 4 :loudness 3 :style 6})
(def cor-anglais (play-gen cor-anglais-samples defaults features distance-maxes))
