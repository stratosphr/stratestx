MACHINE GSM

	CONSTS
		MaxCHV = 3
		MaxUnblock = 10

	SETS
		FILES = {none[0], mf[1], dfgsm[2], eficcid[3], eflp[4], efimsi[5], efad[6]}
		PERMISSION = {always[7], chv[8], never[9], adm[10]}
		VALUE = {true[11], false[12]}
		BLOCKEDSTATUS = {blocked[13], unblocked[14]}
		CODE = {a1[15], a2[16], a3[17], a4[18]}
		DATA = {d1[19], d2[20], d3[21], d4[22]}
		MF = {mf[1]}
		DF = {dfgsm[2]}
		EF = {none[0], eficcid[3], eflp[4], efimsi[5], efad[6]}
		COUNTERCHV = [0..MaxCHV]
		COUNTERUNBLOCKCHV = [0..MaxUnblock]
		STATUSWORD = {sw9000[23], sw9400[24], sw9404[25], sw9804[26], sw9840[27]}

	VARS
		puk : CODE
		currentFile : EF
		currentDirectory : {dfgsm[2], mf[1]}
		counterCHV : COUNTERCHV
		counterUnblockCHV : COUNTERUNBLOCKCHV
		blockedCHVStatus : BLOCKEDSTATUS
		blockedStatus : BLOCKEDSTATUS
		pin : CODE
		sw : STATUSWORD
		dd : {nodata[28], d1[19], d2[20], d3[21], d4[22]}

	FUNS
		filesChildren : {mf[1], dfgsm[2]} -> FILES
		permissionRead : EF -> PERMISSION
		permissionSession : PERMISSION -> VALUE
		data : EF -> DATA

	INVARIANT
		and(
			MaxCHV=3
			MaxUnblock=10
			puk ∈ CODE
			currentFile ∈ EF
			currentDirectory ∈ {dfgsm[2], mf[1]}
			counterCHV ∈ COUNTERCHV
			counterUnblockCHV ∈ COUNTERUNBLOCKCHV
			blockedCHVStatus ∈ BLOCKEDSTATUS
			blockedStatus ∈ BLOCKEDSTATUS
			pin ∈ CODE
			sw ∈ STATUSWORD
			dd ∈ {nodata[28], d1[19], d2[20], d3[21], d4[22]}
			filesChildren(mf[1]) ∈ FILES
			filesChildren(dfgsm[2]) ∈ FILES
			permissionRead(none[0]) ∈ PERMISSION
			permissionRead(eficcid[3]) ∈ PERMISSION
			permissionRead(eflp[4]) ∈ PERMISSION
			permissionRead(efimsi[5]) ∈ PERMISSION
			permissionRead(efad[6]) ∈ PERMISSION
			permissionSession(always[7]) ∈ VALUE
			permissionSession(chv[8]) ∈ VALUE
			permissionSession(never[9]) ∈ VALUE
			permissionSession(adm[10]) ∈ VALUE
			data(none[0]) ∈ DATA
			data(eficcid[3]) ∈ DATA
			data(eflp[4]) ∈ DATA
			data(efimsi[5]) ∈ DATA
			data(efad[6]) ∈ DATA
			and(
				or(
					filesChildren(mf[1])=dfgsm[2]
					filesChildren(mf[1])=eficcid[3]
				)
				or(
					filesChildren(dfgsm[2])=eflp[4]
					filesChildren(dfgsm[2])=efimsi[5]
					filesChildren(dfgsm[2])=efad[6]
				)
				permissionRead(eficcid[3])=never[9]
				permissionRead(eflp[4])=always[7]
				permissionRead(efimsi[5])=chv[8]
				permissionRead(efad[6])=adm[10]
				puk=a3[17]
				permissionSession(always[7])=true[11]
				permissionSession(adm[10])=false[12]
				permissionSession(never[9])=false[12]
				blockedCHVStatus=blocked[13] => permissionSession(chv[8])=false[12]
				counterCHV=0 <=> blockedCHVStatus=blocked[13]
				counterUnblockCHV=0 <=> blockedStatus=blocked[13]
			)
		)

	INITIALISATION
		filesChildren(dfgsm[2]) := efimsi[5] ||
		currentFile := none[0] ||
		currentDirectory := mf[1] ||
		counterCHV := MaxCHV ||
		counterUnblockCHV := MaxUnblock ||
		blockedCHVStatus := unblocked[14] ||
		blockedStatus := unblocked[14] ||
		permissionSession(always[7]) := true[11] ||
		permissionSession(chv[8]) := false[12] ||
		permissionSession(adm[10]) := false[12] ||
		permissionSession(never[9]) := false[12] ||
		pin := a1[15] ||
		data(eficcid[3]) := d1[19] ||
		data(eflp[4]) := d2[20] ||
		data(efimsi[5]) := d3[21] ||
		data(efad[6]) := d4[22] ||
		sw := sw9000[23] ||
		dd := nodata[28]

	EVENTS
		Select_File = 
			ANY
				ff ∈ FILES
			WHERE
				true
			THEN
				IF
					or(
						ff=dfgsm[2]
						ff=mf[1]
					)
				THEN
					IF
						or(
							filesChildren(ff)=currentDirectory
							filesChildren(currentDirectory)=ff
							∃(dp).(and(
								and(
									dp ∈ {dfgsm[2], mf[1]}
								)
								and(
									filesChildren(dp)=currentDirectory
									filesChildren(dp)=ff
								)
							))
							ff=mf[1]
						)
					THEN
						sw := sw9000[23] ||
						currentDirectory := ff ||
						currentFile := none[0]
					ELSE
						sw := sw9404[25]
					END
				ELSE
					IF
						filesChildren(currentDirectory)=ff
					THEN
						sw := sw9000[23] ||
						currentFile := ff
					ELSE
						sw := sw9404[25]
					END
				END
			END

		Read_Binary = 
			IF
				currentFile=none[0]
			THEN
				sw := sw9400[24] ||
				dd := nodata[28]
			ELSE
				IF
					∃(dp).(and(
						and(
							dp ∈ PERMISSION
						)
						and(
							permissionRead(currentFile)=dp
							permissionSession(dp)=true[11]
						)
					))
				THEN
					IF
						currentFile ≠ none[0]
					THEN
						sw := sw9000[23] ||
						dd := data(currentFile)
					ELSE
						sw := sw9000[23]
					END
				ELSE
					sw := sw9804[26] ||
					dd := nodata[28]
				END
			END

		Verify_CHV = 
			ANY
				code ∈ CODE
			WHERE
				true
			THEN
				IF
					blockedCHVStatus=blocked[13]
				THEN
					sw := sw9840[27]
				ELSE
					IF
						pin=code
					THEN
						counterCHV := MaxCHV ||
						permissionSession(chv[8]) := true[11] ||
						sw := sw9000[23]
					ELSE
						IF
							counterCHV=1
						THEN
							counterCHV := 0 ||
							blockedCHVStatus := blocked[13] ||
							permissionSession(chv[8]) := false[12] ||
							sw := sw9840[27]
						ELSE
							counterCHV := (counterCHV - 1) ||
							sw := sw9804[26]
						END
					END
				END
			END

		Unblock_CHV = 
			ANY
				codeUnblock ∈ CODE
				newCode ∈ CODE
			WHERE
				true
			THEN
				IF
					blockedStatus=blocked[13]
				THEN
					sw := sw9840[27]
				ELSE
					IF
						puk=codeUnblock
					THEN
						pin := newCode ||
						blockedCHVStatus := unblocked[14] ||
						counterCHV := MaxCHV ||
						counterUnblockCHV := MaxUnblock ||
						permissionSession(chv[8]) := true[11] ||
						sw := sw9000[23]
					ELSE
						IF
							counterUnblockCHV=1
						THEN
							counterUnblockCHV := 0 ||
							blockedStatus := blocked[13] ||
							sw := sw9840[27]
						ELSE
							counterUnblockCHV := (counterUnblockCHV - 1) ||
							sw := sw9804[26]
						END
					END
				END
			END

		Reset = 
			currentFile := none[0] ||
			currentDirectory := mf[1] ||
			permissionSession(always[7]) := true[11] ||
			permissionSession(chv[8]) := false[12] ||
			permissionSession(adm[10]) := false[12] ||
			permissionSession(never[9]) := false[12]

