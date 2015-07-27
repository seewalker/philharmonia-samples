;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns ^{ :author "Alex Seewald" }
  philharmonia-samples.sampled-cor-anglais
  (:use [philharmonia-samples.sample-utils]
        [overtone.live]))

(def cor-anglais-samples (path-to-described-samples (str sampleroot "/cor-anglais")))
(def defaults (array-map :note "60" :duration "025" :loudness "forte" :style "normal"))
(def features (featureset [:note :duration :loudness :style] (keys cor-anglais-samples)))
(def distance-maxes {:note 1 :duration 4 :loudness 3 :style 6})

(def ^:private tmp (play-gen cor-anglais-samples defaults features distance-maxes))
(def cor-anglais (:ugen tmp))
(def cor-anglais-inst (:effect tmp))
(def cor-anglaisi (:effect tmp))
