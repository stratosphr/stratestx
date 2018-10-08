MACHINE L14_2

	CONSTS
		NS = 4
		NS1 = (NS - 1)
		NR = 3

	SETS
		Rames = [1..NR]
		Stations = [0..NS1]

	FUNS
		Pos : Rames -> Stations
		Dir : Rames -> {-1, 1}
		Portes : Rames -> {ouvertes[0], fermees[1], refermees[2]}
		Mvt : Rames -> [0..1]

	INVARIANT
		and(
			NS=4
			NS1=(NS - 1)
			NR=3
			Pos(1) ∈ Stations
			Pos(2) ∈ Stations
			Pos(3) ∈ Stations
			Dir(1) ∈ {-1, 1}
			Dir(2) ∈ {-1, 1}
			Dir(3) ∈ {-1, 1}
			Portes(1) ∈ {ouvertes[0], fermees[1], refermees[2]}
			Portes(2) ∈ {ouvertes[0], fermees[1], refermees[2]}
			Portes(3) ∈ {ouvertes[0], fermees[1], refermees[2]}
			Mvt(1) ∈ [0..1]
			Mvt(2) ∈ [0..1]
			Mvt(3) ∈ [0..1]
			and(
				∀(R).(and(
					R ∈ Rames
				) => and(
					Mvt(R)=1
				) => Portes(R)=fermees[1])
				∀(R).(and(
					R ∈ Rames
				) => Portes(R)=ouvertes[0] => Mvt(R)=0)
				∀(R).(and(
					R ∈ Rames
				) => Portes(R)=refermees[2] => Mvt(R)=0)
				∀(R).(and(
					R ∈ Rames
				) => Dir(R) ≠ 0)
				∀(R1, R2).(and(
					R1 ∈ Rames
					R2 ∈ Rames
				) => R1 ≠ R2 => or(
					Dir(R1) ≠ Dir(R2)
					Pos(R1) ≠ Pos(R2)
					Mvt(R1) ≠ Mvt(R2)
				))
				∀(R).(and(
					R ∈ Rames
				) => and(
					Dir(R)=-1
					Pos(R)=0
				) => Mvt(R)=0)
				∀(R).(and(
					R ∈ Rames
				) => and(
					Dir(R)=1
					Pos(R)=NS1
				) => Mvt(R)=0)
			)
		)

	INITIALISATION
		Pos(1) := 0 ||
		Pos(2) := 1 ||
		Pos(3) := 1 ||
		Dir(1) := -1 ||
		Dir(2) := -1 ||
		Dir(3) := -1 ||
		Mvt(1) := 0 ||
		Mvt(2) := 1 ||
		Mvt(3) := 0 ||
		Portes(1) := fermees[1] ||
		Portes(2) := fermees[1] ||
		Portes(3) := fermees[1]

	EVENTS
		DS1 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=1
					Portes(R)=refermees[2]
					Mvt(R)=0
					or(
						Pos(R) ≠ NS1
						Dir(R) ≠ 1
					)
					or(
						Pos(R) ≠ 0
						Dir(R) ≠ -1
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => R1 ≠ R => or(
						Pos(R1) ≠ Pos(R)
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Mvt(R) := 1
			END

		DS2 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=2
					Portes(R)=refermees[2]
					Mvt(R)=0
					or(
						Pos(R) ≠ NS1
						Dir(R) ≠ 1
					)
					or(
						Pos(R) ≠ 0
						Dir(R) ≠ -1
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => R1 ≠ R => or(
						Pos(R1) ≠ Pos(R)
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Mvt(R) := 1
			END

		DS3 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=3
					Portes(R)=refermees[2]
					Mvt(R)=0
					or(
						Pos(R) ≠ NS1
						Dir(R) ≠ 1
					)
					or(
						Pos(R) ≠ 0
						Dir(R) ≠ -1
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => R1 ≠ R => or(
						Pos(R1) ≠ Pos(R)
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Mvt(R) := 1
			END

		AS1 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=1
					Mvt(R)=1
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => or(
						Pos(R1) ≠ (Pos(R) + Dir(R))
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Pos(R) := (Pos(R) + Dir(R)) ||
				Mvt(R) := 0
			END

		AS2 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=2
					Mvt(R)=1
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => or(
						Pos(R1) ≠ (Pos(R) + Dir(R))
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Pos(R) := (Pos(R) + Dir(R)) ||
				Mvt(R) := 0
			END

		AS3 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=3
					Mvt(R)=1
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => or(
						Pos(R1) ≠ (Pos(R) + Dir(R))
						Dir(R1) ≠ Dir(R)
					))
				)
			THEN
				Pos(R) := (Pos(R) + Dir(R)) ||
				Mvt(R) := 0
			END

		CD1 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=1
					Portes(R)=refermees[2]
					or(
						and(
							Pos(R)=NS1
							Dir(R)=1
						)
						and(
							Pos(R)=0
							Dir(R)=-1
						)
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => and(
						Pos(R1) ≠ Pos(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Dir(R) := (-1 * Dir(R))
			END

		CD2 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=2
					Portes(R)=refermees[2]
					or(
						and(
							Pos(R)=NS1
							Dir(R)=1
						)
						and(
							Pos(R)=0
							Dir(R)=-1
						)
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => and(
						Pos(R1) ≠ Pos(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Dir(R) := (-1 * Dir(R))
			END

		CD3 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=3
					Portes(R)=refermees[2]
					or(
						and(
							Pos(R)=NS1
							Dir(R)=1
						)
						and(
							Pos(R)=0
							Dir(R)=-1
						)
					)
					∀(R1).(and(
						R1 ∈ Rames
					) => and(
						R1 ≠ R
					) => and(
						Pos(R1) ≠ Pos(R)
					))
				)
			THEN
				Portes(R) := fermees[1] ||
				Dir(R) := (-1 * Dir(R))
			END

		OP1 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=1
					Portes(R)=fermees[1]
					Mvt(R)=0
				)
			THEN
				Portes(R) := ouvertes[0]
			END

		OP2 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=2
					Portes(R)=fermees[1]
					Mvt(R)=0
				)
			THEN
				Portes(R) := ouvertes[0]
			END

		OP3 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=3
					Portes(R)=fermees[1]
					Mvt(R)=0
				)
			THEN
				Portes(R) := ouvertes[0]
			END

		FP1 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=1
					Portes(R)=ouvertes[0]
					Mvt(R)=0
				)
			THEN
				Portes(R) := refermees[2]
			END

		FP2 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=2
					Portes(R)=ouvertes[0]
					Mvt(R)=0
				)
			THEN
				Portes(R) := refermees[2]
			END

		FP3 = 
			ANY
				R ∈ Rames
			WHERE
				and(
					R=3
					Portes(R)=ouvertes[0]
					Mvt(R)=0
				)
			THEN
				Portes(R) := refermees[2]
			END

