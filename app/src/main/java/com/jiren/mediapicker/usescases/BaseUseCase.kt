package com.jiren.mediapicker.usescases

interface  BaseUseCase <in Input, out Output> {
    /**
     * metodo de ejecucion base
     * @param params define tipo de filto
     * return output caso de uso
     */
    suspend operator fun invoke(params: Input): Output
}