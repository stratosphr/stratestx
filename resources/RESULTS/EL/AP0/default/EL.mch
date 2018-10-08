MACHINE EL

	CONSTS
		n = 7

	VARS
		sw : [1..n]
		h : {tic[0], tac[1]}

	FUNS
		bat : [1..n] -> {ko[2], ok[3]}

	INVARIANT
		and(
			n=7
			sw ∈ [1..n]
			h ∈ {tic[0], tac[1]}
			bat(1) ∈ {ko[2], ok[3]}
			bat(2) ∈ {ko[2], ok[3]}
			bat(3) ∈ {ko[2], ok[3]}
			bat(4) ∈ {ko[2], ok[3]}
			bat(5) ∈ {ko[2], ok[3]}
			bat(6) ∈ {ko[2], ok[3]}
			bat(7) ∈ {ko[2], ok[3]}
			bat(sw)=ok[3]
		)

	INITIALISATION
		h := tac[1] ||
		sw := 1 ||
		bat(1) := ok[3] ||
		bat(2) := ok[3] ||
		bat(3) := ok[3] ||
		bat(4) := ok[3] ||
		bat(5) := ok[3] ||
		bat(6) := ok[3] ||
		bat(7) := ok[3]

	EVENTS
		Tic = 
			SELECT
				h=tac[1]
			THEN
				h := tic[0]
			END

		Commute = 
			ANY
				i ∈ [1..n]
				j ∈ [1..n]
			WHERE
				and(
					i ≠ j
					bat(i)=ok[3]
					bat(j)=ok[3]
					h=tic[0]
				)
			THEN
				ANY
					ns ∈ [1..n]
				WHERE
					and(
						bat(ns)=ok[3]
						ns ≠ sw
					)
				THEN
					h := tac[1] ||
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
					bat(i)=ok[3]
					bat(j)=ok[3]
				)
			THEN
				ANY
					nb ∈ [1..n]
				WHERE
					and(
						bat(nb)=ok[3]
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
									bat(ns)=ok[3]
								)
							THEN
								sw := ns ||
								bat(nb) := ko[2]
							END
						END
					OR
						SELECT
							nb ≠ sw
						THEN
							bat(nb) := ko[2]
						END
					END
				END
			END

		Repair = 
			ANY
				nb ∈ [1..n]
			WHERE
				and(
					bat(nb)=ko[2]
				)
			THEN
				bat(nb) := ok[3]
			END

