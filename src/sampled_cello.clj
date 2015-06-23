;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly because the buffers are large.
(ns { :author "Alex Seewald" }
sampled-cello
  (:use [sample-utils]
        [overtone.live]))

(def cello-samples (path-to-described-samples "samples/cello"))
(def ^:private defaults {:note "As3" :duration "phrase" :loudness "forte" :style "arco-normal"})
(def cello (play-gen cello-samples defaults))
