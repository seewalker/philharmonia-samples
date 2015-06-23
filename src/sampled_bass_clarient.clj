;the sense in making a file for each instrument is that people can include them in their own namespaces individually, which can be costly because the buffers are large.
(ns { :author "Alex Seewald" }
sampled-bass-clarinet
  (:use [sample-utils]
        [overtone.live]))

(def bass-clarinet-samples (path-to-described-samples "samples/bass-clarinet"))
(def ^:private defaults {:note "As3" :duration "05" :loudness "forte" :style "normal"})
(def bass-clarinet (play-gen bass-clarinet-samples defaults))
