MACHINE EL

	CONSTS
		n = 8

	VARS
		sw : [1..n]
		h : {tic[6], tac[7]}

	FUNS
		bat : [1..n] -> {ko[8], ok[9]}

	INVARIANT
		and(
			n=8
			sw ∈ [1..n]
			h ∈ {tic[6], tac[7]}
			bat(1) ∈ {ko[8], ok[9]}
			bat(2) ∈ {ko[8], ok[9]}
			bat(3) ∈ {ko[8], ok[9]}
			bat(4) ∈ {ko[8], ok[9]}
			bat(5) ∈ {ko[8], ok[9]}
			bat(6) ∈ {ko[8], ok[9]}
			bat(7) ∈ {ko[8], ok[9]}
			bat(8) ∈ {ko[8], ok[9]}
			bat(sw)=ok[9]
		)

	INITIALISATION
		h := tac[7] ||
		sw := 1 ||
		bat(1) := ok[9] ||
		bat(2) := ok[9] ||
		bat(3) := ok[9] ||
		bat(4) := ok[9] ||
		bat(5) := ok[9] ||
		bat(6) := ok[9] ||
		bat(7) := ok[9] ||
		bat(8) := ok[9]

	EVENTS
		Tic = 
			SELECT
				h=tac[7]
			THEN
				h := tic[6]
			END

		Commute = 
			ANY
				i ∈ [1..n]
				j ∈ [1..n]
			WHERE
				and(
					i ≠ j
					bat(i)=ok[9]
					bat(j)=ok[9]
					h=tic[6]
				)
			THEN
				ANY
					ns ∈ [1..n]
				WHERE
					and(
						bat(ns)=ok[9]
						ns ≠ sw
					)
				THEN
					h := tac[7] ||
					sw := ns
				END
			END

		Fail = 
			ANY
				i ∈ [1..n]
				j ∈ [1..n]
			WHERE
				and(
					i ≠ j
					bat(i)=ok[9]
					bat(j)=ok[9]
				)
			THEN
				ANY
					nb ∈ [1..n]
				WHERE
					and(
						bat(nb)=ok[9]
					)
				THEN
					CHOICE
						SELECT
							nb=sw
						THEN
							ANY
								ns ∈ [1..n]
							WHERE
								and(
									ns ≠ sw
									bat(ns)=ok[9]
								)
							THEN
								sw := ns ||
								bat(nb) := ko[8]
							END
						END
					OR
						SELECT
							nb ≠ sw
						THEN
							bat(nb) := ko[8]
						END
					END
				END
			END

		Repair = 
			ANY
				nb ∈ [1..n]
			WHERE
				and(
					bat(nb)=ko[8]
				)
			THEN
				bat(nb) := ok[9]
			END

