;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly
;because the buffers are large.
(ns { :author "Alex Seewald" }
sampled-banjo
  (:use [sample-utils]
        [overtone.live]))
(def banjo-samples (path-to-described-samples "samples/banjo"))
(def ^:private defaults {:note "As3" :duration "very-long" :loudness "forte" :style "normal"})
(def banjo (play-gen banjo-samples defaults))
