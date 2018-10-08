MACHINE GSM

	CONSTS
		MaxCHV = 3
		MaxUnblock = 10

	SETS
		FILES = {none[10], mf[11], dfgsm[12], eficcid[13], eflp[14], efimsi[15], efad[16]}
		PERMISSION = {always[17], chv[18], never[19], adm[20]}
		VALUE = {true[21], false[22]}
		BLOCKEDSTATUS = {blocked[23], unblocked[24]}
		CODE = {a1[25], a2[26], a3[27], a4[28]}
		DATA = {d1[29], d2[30], d3[31], d4[32]}
		MF = {mf[11]}
		DF = {dfgsm[12]}
		EF = {none[10], eficcid[13], eflp[14], efimsi[15], efad[16]}
		COUNTERCHV = [0..MaxCHV]
		COUNTERUNBLOCKCHV = [0..MaxUnblock]
		STATUSWORD = {sw9000[33], sw9400[34], sw9404[35], sw9804[36], sw9840[37]}

	VARS
		puk : CODE
		currentFile : EF
		currentDirectory : {dfgsm[12], mf[11]}
		counterCHV : COUNTERCHV
		counterUnblockCHV : COUNTERUNBLOCKCHV
		blockedCHVStatus : BLOCKEDSTATUS
		blockedStatus : BLOCKEDSTATUS
		pin : CODE
		sw : STATUSWORD
		dd : {nodata[38], d1[29], d2[30], d3[31], d4[32]}

	FUNS
		filesChildren : {mf[11], dfgsm[12]} -> FILES
		permissionRead : EF -> PERMISSION
		permissionSession : PERMISSION -> VALUE
		data : EF -> DATA

	INVARIANT
		and(
			MaxCHV=3
			MaxUnblock=10
			puk ∈ CODE
			currentFile ∈ EF
			currentDirectory ∈ {dfgsm[12], mf[11]}
			counterCHV ∈ COUNTERCHV
			counterUnblockCHV ∈ COUNTERUNBLOCKCHV
			blockedCHVStatus ∈ BLOCKEDSTATUS
			blockedStatus ∈ BLOCKEDSTATUS
			pin ∈ CODE
			sw ∈ STATUSWORD
			dd ∈ {nodata[38], d1[29], d2[30], d3[31], d4[32]}
			filesChildren(mf[11]) ∈ FILES
			filesChildren(dfgsm[12]) ∈ FILES
			permissionRead(none[10]) ∈ PERMISSION
			permissionRead(eficcid[13]) ∈ PERMISSION
			permissionRead(eflp[14]) ∈ PERMISSION
			permissionRead(efimsi[15]) ∈ PERMISSION
			permissionRead(efad[16]) ∈ PERMISSION
			permissionSession(always[17]) ∈ VALUE
			permissionSession(chv[18]) ∈ VALUE
			permissionSession(never[19]) ∈ VALUE
			permissionSession(adm[20]) ∈ VALUE
			data(none[10]) ∈ DATA
			data(eficcid[13]) ∈ DATA
			data(eflp[14]) ∈ DATA
			data(efimsi[15]) ∈ DATA
			data(efad[16]) ∈ DATA
			and(
				or(
					filesChildren(mf[11])=dfgsm[12]
					filesChildren(mf[11])=eficcid[13]
				)
				or(
					filesChildren(dfgsm[12])=eflp[14]
					filesChildren(dfgsm[12])=efimsi[15]
					filesChildren(dfgsm[12])=efad[16]
				)
				permissionRead(eficcid[13])=never[19]
				permissionRead(eflp[14])=always[17]
				permissionRead(efimsi[15])=chv[18]
				permissionRead(efad[16])=adm[20]
				puk=a3[27]
				permissionSession(always[17])=true[21]
				permissionSession(adm[20])=false[22]
				permissionSession(never[19])=false[22]
				blockedCHVStatus=blocked[23] => permissionSession(chv[18])=false[22]
				counterCHV=0 <=> blockedCHVStatus=blocked[23]
				counterUnblockCHV=0 <=> blockedStatus=blocked[23]
			)
		)

	INITIALISATION
		filesChildren(dfgsm[12]) := efimsi[15] ||
		currentFile := none[10] ||
		currentDirectory := mf[11] ||
		counterCHV := MaxCHV ||
		counterUnblockCHV := MaxUnblock ||
		blockedCHVStatus := unblocked[24] ||
		blockedStatus := unblocked[24] ||
		permissionSession(always[17]) := true[21] ||
		permissionSession(chv[18]) := false[22] ||
		permissionSession(adm[20]) := false[22] ||
		permissionSession(never[19]) := false[22] ||
		pin := a1[25] ||
		data(eficcid[13]) := d1[29] ||
		data(eflp[14]) := d2[30] ||
		data(efimsi[15]) := d3[31] ||
		data(efad[16]) := d4[32] ||
		sw := sw9000[33] ||
		dd := nodata[38]

	EVENTS
		Select_File = 
			ANY
				ff ∈ FILES
			WHERE
				true
			THEN
				IF
					or(
						ff=dfgsm[12]
						ff=mf[11]
					)
				THEN
					IF
						or(
							filesChildren(ff)=currentDirectory
							filesChildren(currentDirectory)=ff
							∃(dp).(and(
								and(
									dp ∈ {dfgsm[12], mf[11]}
								)
								and(
									filesChildren(dp)=currentDirectory
									filesChildren(dp)=ff
								)
							))
							ff=mf[11]
						)
					THEN
						sw := sw9000[33] ||
						currentDirectory := ff ||
						currentFile := none[10]
					ELSE
						sw := sw9404[35]
					END
				ELSE
					IF
						filesChildren(currentDirectory)=ff
					THEN
						sw := sw9000[33] ||
						currentFile := ff
					ELSE
						sw := sw9404[35]
					END
				END
			END

		Read_Binary = 
			IF
				currentFile=none[10]
			THEN
				sw := sw9400[34] ||
				dd := nodata[38]
			ELSE
				IF
					∃(dp).(and(
						and(
							dp ∈ PERMISSION
						)
						and(
							permissionRead(currentFile)=dp
							permissionSession(dp)=true[21]
						)
					))
				THEN
					IF
						currentFile ≠ none[10]
					THEN
						sw := sw9000[33] ||
						dd := data(currentFile)
					ELSE
						sw := sw9000[33]
					END
				ELSE
					sw := sw9804[36] ||
					dd := nodata[38]
				END
			END

		Verify_CHV = 
			ANY
				code ∈ CODE
			WHERE
				true
			THEN
				IF
					blockedCHVStatus=blocked[23]
				THEN
					sw := sw9840[37]
				ELSE
					IF
						pin=code
					THEN
						counterCHV := MaxCHV ||
						permissionSession(chv[18]) := true[21] ||
						sw := sw9000[33]
					ELSE
						IF
							counterCHV=1
						THEN
							counterCHV := 0 ||
							blockedCHVStatus := blocked[23] ||
							permissionSession(chv[18]) := false[22] ||
							sw := sw9840[37]
						ELSE
							counterCHV := (counterCHV - 1) ||
							sw := sw9804[36]
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
					blockedStatus=blocked[23]
				THEN
					sw := sw9840[37]
				ELSE
					IF
						puk=codeUnblock
					THEN
						pin := newCode ||
						blockedCHVStatus := unblocked[24] ||
						counterCHV := MaxCHV ||
						counterUnblockCHV := MaxUnblock ||
						permissionSession(chv[18]) := true[21] ||
						sw := sw9000[33]
					ELSE
						IF
							counterUnblockCHV=1
						THEN
							counterUnblockCHV := 0 ||
							blockedStatus := blocked[23] ||
							sw := sw9840[37]
						ELSE
							counterUnblockCHV := (counterUnblockCHV - 1) ||
							sw := sw9804[36]
						END
					END
				END
			END

		Reset = 
			currentFile := none[10] ||
			currentDirectory := mf[11] ||
			permissionSession(always[17]) := true[21] ||
			permissionSession(chv[18]) := false[22] ||
			permissionSession(adm[20]) := false[22] ||
			permissionSession(never[19]) := false[22]

