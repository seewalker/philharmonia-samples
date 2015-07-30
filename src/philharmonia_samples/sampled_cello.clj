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
(defn avril14 [beat it]
  (let [righthand (fn []
                    (do
                        (after-delay 0 #(celloi :note "C5"))
                        (after-delay (* beat 1) #(celloi :note "F4"))
                        (after-delay (* beat 2) #(celloi :note "Ab4"))
                        (after-delay (* beat 3.5) #(celloi :note "Ab4"))
                        (after-delay (* beat 3.75) #(celloi :note "F4"))))
        lefthand (fn []
                   (do
                   (after-delay 0 #(celloi :note "Ab2"))
                   (after-delay (* beat 0.5) #(celloi :note "F2"))
                   (after-delay (* beat 1) #(celloi :note "Ab3"))
                   (after-delay (* beat 1.5) #(celloi :note "C3"))
                   (after-delay (* beat 2) #(celloi :note "C2"))
                   (after-delay (* beat 2.5) #(celloi :note "Ab3"))
                   (after-delay (* beat 3) #(celloi :note "C4"))
                   (after-delay (* beat 3.5) #(celloi :note "Eb4"))))]
    (lefthand)
    (righthand)
    (apply-at (+ (now) (* beat 4)) #'phrase [beat (+ it 1)])))
