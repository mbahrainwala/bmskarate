import React, {useState} from 'react';

import {
  FormControl,
  FormLabel,
  InputLabel,
  Input,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Button
} from "@material-ui/core";

import {restCall} from '../utils/RestComponent'


const ListStudents = props => {
    const [apiRet, setApiRet] = useState(
        props.listFor==='remove'?props.globalData.editUser.students:{});

    const handleChange=(event)=>{
        if(event.target.id==='lastName'){
            const showLinked = props.listFor==='add'?'N':'Y';
            restCall('GET', `${props.serverURI}/api/findStudents?lastName=${event.target.value}&showLinked=${showLinked}`, setApiRet, props.token, null);
        }
    }

    const [apiRetAdd, setApiRetAdd] = useState({});
    const addStudent=(userId, studentId)=>{
        restCall('PATCH', `${props.globalData.serverURI}/api/addStudentToUser?userId=${userId}&studentId=${studentId}`, setApiRetAdd, props.globalData.token, null);
    }

    const removeStudent=(userId, studentId)=>{
            restCall('PATCH', `${props.globalData.serverURI}/api/removeStudentFromUser?userId=${userId}&studentId=${studentId}`, setApiRetAdd, props.globalData.token, null);
        }

    return(
        <>
            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
               <div style={{width:"80%"}}>
                    <form onChange={handleChange.bind(this)}>
                        {props.listFor==='find'?(<h1>List Students</h1>):null}
                        {props.listFor==='add'?(<h1>Add Students</h1>):null}
                        {props.listFor==='remove'?(<h1>Students</h1>):null}
                        {apiRet.error !== undefined?(<FormLabel component="legend">{apiRet.error}</FormLabel>):(<></>)}
                        {apiRetAdd.error !== undefined?(<FormLabel component="legend">{apiRetAdd.error}</FormLabel>):null}

                        {props.listFor!=='remove'?(
                        <FormControl margin="normal" fullWidth>
                            <InputLabel htmlFor="lastName">Last Name</InputLabel>
                            <Input id="lastName" type="text"/>
                        </FormControl>):null}
                    </form>
                </div>
            </div>

            <div
                style={{
                display: "flex",
                justifyContent: "center",
                margin: 20,
                padding: 20
                }}
            >
            <TableContainer component={Paper} style={{width:"80%"}}>
                <Table aria-label="simple table">
                <TableHead>
                  <TableRow>
                    <TableCell>First Name</TableCell>
                    <TableCell>Last Name</TableCell>
                    <TableCell>Number</TableCell>
                    <TableCell>Belt</TableCell>
                    <TableCell>Stripes</TableCell>
                    {props.listFor==='find'?(<TableCell>Parent Last Name</TableCell>):null}
                    <TableCell>&nbsp;</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                    {apiRet!== undefined
                        && apiRet!==null && apiRet.error === undefined
                        && Object.getOwnPropertyNames(apiRet).length !== 0?(
                        <>
                            {apiRet.map(student=>(
                                <TableRow key={student.id}>
                                    <TableCell component="th" scope="row">
                                        {student.firstName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.lastName}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.number}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {props.globalData.commonData.belts.map(belt=>(
                                            <>{belt.beltId===student.belt?(<>{belt.beltColor}</>):null}</>
                                        ))}
                                    </TableCell>
                                    <TableCell scope="row">
                                        {student.stripes}
                                    </TableCell>
                                    {props.listFor==='find'?(
                                    <TableCell scope="row">
                                        {student.parent!==null && student.parent !== undefined?(
                                            <>{student.parent.lastName}, {student.parent.firstName}</>
                                        ):null}
                                    </TableCell>):null}
                                    <TableCell scope="row">
                                        {props.listFor==='find'?(
                                        <Button onClick={()=>{
                                            const newProps = {...props.globalData};
                                            newProps.appPage='addEditStudent';
                                            newProps.studentId=student.id;
                                            props.setGlobalData(newProps);
                                            }}>Edit</Button>):null}
                                         {props.listFor==='add'?(
                                             <Button color="primary" variant="contained" size="small"
                                                onClick={()=>{addStudent(props.globalData.editUser.id, student.id)}}>
                                                Add</Button>
                                            ):null}
                                         {props.listFor==='remove'?(
                                              <Button color="primary" variant="contained" size="small"
                                                 onClick={()=>{removeStudent(props.globalData.editUser.id, student.id)}}>
                                                 Remove</Button>
                                             ):null}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </>
                    ):(null)}
                </TableBody>
                </Table>
            </TableContainer>
            </div>
            </>
    );
}

export default ListStudents;