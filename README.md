# What does this project do for you?

This project provides an interface to work with a variety of instrument samples from the London Philharmonic Orchestra, either as ugens or as clojure functions which produce sound effects when called. There is exactly one module per sample instrument. Each module provides a function, of the same name as the instrument, which returns a ugen suitable for use with overtone. A simple way to describe a ugen might be a composable, commutable unit of sound specification; for more information see. Each module also provides a function which accepts the same form of input, but which affects sounds rather than returning a ugen. The 'definst' infrastructure is not used because these sampled instruments have many variants, e.g. vibrato or non-vibrato, of the same note.

# How to get things going:

To use it with leiningen, put this in your project.clj
`[org.seewalker.philharmonia "0.0.1"]`

The sample data itself is on the order of gigabyes, so it is not feasible to host it on github or clojars along with the code. For now, the place to get them is [my google drive](https://drive.google.com/open?id=0B7GoGDjZUyZ5MUxsVmQxVzJOTVE). Note, this URL plays nicely with web browsers but not utilities like `curl`. The reason to download them from here rather than the philharmonic website is that they provide .mp3s, whereas overtone requires .wavs, and I've done the work of converting them and packaging them as a single zip.

When you've downloaded that, unzip it into the `resources` folder of your leiningen project.

# How to use the sampled instrument functions

Let's use the example of a cello, because all the samples instruments are similar to this.

##loading samples

`(use 'philharmonia-samples.sampled-cello)`

assuming philharmonia-samples/src is in your classpath.

`(:require [philharmonia-samples.sampled-cello :refer [cello celloi cello-inst]])`

all the sampled instrument files have similar information in them, sharing the same name, so you should probably 'require' rather than 'use' it, and you likely don't care about those other names, so you could just refer the ugen producing function (cello) and the instrument-like function (celloi).

You may want to only refer celloi if you are exclusively live-coding (for sake of less typing), or you may want to refer cello-inst if you are writing a program you intend to re-use (for sake of a semantic, rememberable name).

Loading all the samples associated with an instrument can produce a var taking up as little as 21MB, in the case of the mandolin, and as much as 169 MB, in the case of the violin. For this reason, the samples are grouped into separate files by instrument.

## calling samples

`(cello)`

produces a ugen playing the default sample.

`(celloi)`

will make your speakers play the default sample

`(cello-inst)`

 equivalent to above

`(demo (cello))`

 equivalent to above

`(cello {:note "A#3" :loudness "piano" :style "vibrato" :duration "025"])`

you can specify all parameters in a map.

`(cello {:loudness "forte" :style "vibrato" :duration "025" :note "G4"])`

order does not matter in the map, so this will also produce a sound.

`(cello {:note "A#3"})`

you can specify only the parameters you wish, and the default value for each unspecified parameter gets filled in.

`(cello :note "A#3" :style "vibrato")`

or, since overtone is meant for live coding, you can save some precious keystrokes by passing keys and values instead of an explicit map.

`(cello {:note 60})`

passing an integer will make the value interpreted as a midi note, rather than a note name.

`(cello 60)`

'note' is arguably the most significant feature, so a single scalar argument will be interpreted as a note.

`(cello ["A#3" "piano" "vibrato"])`

when passing a vector, you must specify all features.

`(celloi :note 58)`

if your use case lends itself to functions that produce sounds (rather than ugens) when called, you can use function with 'i' as a suffix, which is otherwise identical in how it's used.

Because `(cello)` produces a ugen, it could be used like so:

    (defcgen [note {:default 60}]
        (:ar
           (let [freq (midicps note)]
             (+ (sin-osc note)
                (cello note)
                (white-noise)))))

This simply overlaps the sounds of a sine wave playing a frequency, a cello playing a note which the human ear predominantly associates with that same frequency, and some white noise.

For clarifications sake, a string note value follows the overtone naming conventions and an integer note value has MIDI units.

# Scheduling 

Overtone seems to provide some special infrastructure for things it considers instruments. This project does not make its sampled instruments with overtone's definst macro, so some things like `at` do not work as one expects it to work with instruments. Instead, scheduling should be done as is done with raw ugens (or arbitrary clojure functions, for that matter). I always use the `after-delay` function, which is exported by the `overtone.live` module, like so:

`(after-delay 1000 #(celloi :note "C4"))`

schedules a cello to play the a "C4" note in 1000 milliseconds (one second).

`(after-delay 0 #(demo (sin-osc 440)))`

schedules a sine-wave to be played now.

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

Plays the first bar of 'Avril 14' by Aphex Twin on the cello.

# error handling

- If you specify a parameter that does not exist, that information is discarded and the default value for the parameter you meant is used.
- If you specify a value that a parameter cannot take, autocorrection is attempted. If no suitable correction, the default value is used.
- If you specify a combination of allowed and parameters that has no corresponding sample, an error message is printed and the expression evaluates to false.

It may make sense to make the decision of whether to the use of default values be a configurable thing.
Rather than simply printing an error message when there is no corresponding sample for what is requested, it may make more sense to try to play a sample most similar to the requested sample, but that's a lot of effort and this project is not currently in heavy enough use for that to pay off.

# customizations

- You can scale up or scale back how eagerly misspellings are autocorrected for each parameter. The `distance-maxes` var in each sampled instrument file has keys corresponding to the parameters, and values corresponding to the maximum string distance between allowed values and values that will be corrected. Note, these values can be set to '0' if you do not want any autocorrection.
- You can change what the default parameters for an instrument are by changing the `defaults` var.

# Welcome Changes

- The 1.0.0 version of this project might involve more precise names for the instrument features. For example, I chose "loudness" as the name for the class of features like "piano" and "forte", and "style" as the name for features like "arco-normal", but this might not be the terminology musicians want to work with.
- Of course, more samples and a better multi-gigabye public storage solution would be a plus.
- A mechanism to produce a true overtone instrument rather than the current 'cello-inst' system might make more sense, but I found it difficult to get parameters into instruments and the current solution works okay.
